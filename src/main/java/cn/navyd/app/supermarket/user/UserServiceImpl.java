package cn.navyd.app.supermarket.user;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.base.AbstractBaseService;
import cn.navyd.app.supermarket.base.DuplicateException;
import cn.navyd.app.supermarket.base.NotFoundException;
import cn.navyd.app.supermarket.role.RoleDO;
import cn.navyd.app.supermarket.role.RoleService;
import cn.navyd.app.supermarket.user.authentication.DisabledException;
import cn.navyd.app.supermarket.user.authentication.EmailRegisterService;
import cn.navyd.app.supermarket.user.authentication.IncorrectPasswordException;
import cn.navyd.app.supermarket.user.authentication.LockedException;
import cn.navyd.app.supermarket.user.authentication.RegisterUserForm;
import cn.navyd.app.supermarket.user.reset.EmailForgotPasswordService;
import cn.navyd.app.supermarket.user.reset.OldPasswordUserForm;
import cn.navyd.app.supermarket.user.reset.SecureCodeUserForm;
import cn.navyd.app.supermarket.user.securecode.IncorrectSecureCodeException;
import cn.navyd.app.supermarket.user.securecode.SecureCodeNotFoundException;
import cn.navyd.app.supermarket.user.securecode.SecureCodeService;
import cn.navyd.app.supermarket.userrole.UserRoleService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class UserServiceImpl extends AbstractBaseService<UserDO> implements UserService {
  private final UserDao userDao;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UserRoleService userRoleService;
  @Autowired
  private RoleService roleService;
  @Autowired
  private EmailRegisterService emailRegisterService;
  @Autowired
  private EmailForgotPasswordService emailForgotPasswordService;
  
  @Autowired
  public UserServiceImpl(UserDao userDao) {
    super(userDao);
    this.userDao = userDao;
  }

  @Override
  protected void checkAssociativeNotFound(UserDO bean) throws NotFoundException {
  }

  @Override
  protected DuplicateException createDuplicateException(String message) {
    return new DuplicateUserException(message);
  }

  @Override
  protected NotFoundException createNotFoundException(String message) {
    return new UserNotFoundException(message);
  }

  @Override
  public Optional<UserDO> getByUsername(String username) {
    Objects.requireNonNull(username);
    UserDO user = userDao.getByUsername(username);
    return Optional.ofNullable(user);
  }

  @Override
  public Optional<UserDO> getByEmail(String email) {
    checkArgument(!StringUtils.isEmpty(email) && !StringUtils.isBlank(email), "email: %s", email);
    return Optional.ofNullable(userDao.getByEmail(email));
  }

  @Transactional
  @Override
  public UserDO register(RegisterUserForm registerUser) {
    Objects.requireNonNull(registerUser);
    final String username = registerUser.getUsername(), password = registerUser.getPassword(),
        email = registerUser.getEmail();
    // 检查username不存在
    Optional<UserDO> user = getByUsername(username);
    if (user.isPresent())
      throw new DuplicateUserException("username: " + username);
    // 检查email
    if (getByEmail(email).isPresent())
      throw new DuplicateUserException("email: " + email);
    // 检查安全码
    String code = registerUser.getCode();
    checkSecureCode(emailRegisterService, email, code);
    // 保存用户
    UserDO newUser = new UserDO();
    // password加密
    checkPassword(password);
    String hashPassword = passwordEncoder.encode(password);
    newUser.setHashPassword(hashPassword);
    newUser.setUsername(username);
    newUser.setEmail(email);
    newUser.setPhoneNumber(registerUser.getPhoneNumber());
    newUser.setIconPath(registerUser.getIconPath());
    newUser.setEnabled(true);
    return save(newUser);
  }
  
  @Override
  public void sendRegisteringCodeByEmail(String email) {
    // 是否合法
    checkNotNull(email);
    // email是否被注册
    if (getByEmail(email).isPresent())
      throw new DuplicateUserException("email: " + email);
    // 未注册则发送
    emailRegisterService.sendCode(email);
  }
  
  @Override
  public void sendForgotPasswordCodeByEmail(String email) {
    Objects.requireNonNull(email);
    // user是否存在
    if (getByEmail(email).isEmpty())
      throw new UserNotFoundException("email: " + email);
    // 发送邮件
    emailForgotPasswordService.sendCode(email);
  }
  
  @Override
  public UserDO resetPassword(SecureCodeUserForm user) {
    checkNotNull(user);
    final Integer id = user.getId();
    final String resetCode = user.getCode();
    // 检查user是否存在
    UserDO existingUser = checkNotFoundByPrimaryKey(id);
    // 检查用户email是否对应了code
    checkSecureCode(emailForgotPasswordService, existingUser.getEmail(), resetCode);
    return resetPassword0(existingUser, user.getNewPassword());
  }
  
  @Override
  public UserDO resetPassword(OldPasswordUserForm user) {
    checkNotNull(user);
    UserDO existingUser = checkNotFoundByPrimaryKey(user.getId());
    final String oldPassword = user.getOldPassword(), 
        oldHashPassword = existingUser.getHashPassword(), 
        newPassword = user.getNewPassword();
    // 检查旧密码是否正确
    if (!passwordEncoder.matches(oldPassword, oldHashPassword))
      throw new IncorrectPasswordException("旧密码不正确");
    return resetPassword0(existingUser, newPassword);
  }
  
  /**
   * 用户登录操作
     * <ol>
     * <li>如果 未激活{@link UserDO#getEnabled()} 或 被锁定 则抛出异常
     * <li>如果用户密码错误则更新failCount并抛出异常
     * <li>如果登录成功则清除failCount并返回
     * </ol>
   */
  @Transactional
  @Override
  public UserDO login(String username, String password) {
    checkNotNull(username);
    checkNotNull(password);
    Optional<UserDO> user = getByUsername(username);
    if (!user.isPresent())
      throw new UserNotFoundException("username: " + username);
    final UserDO existingUser = user.get();
    GenericUser genericUser = GenericUser.of(user.get());
    // 账户未激活
    if (!genericUser.isEnabled())
      throw new DisabledException();
    // 账户已锁定
    if (genericUser.isLocked()) {
      throw new LockedException();
    }
    // 密码错误  设置登录失败次数
    if (!passwordEncoder.matches(password, existingUser.getHashPassword())) {
      UserDO updateUser = new UserDO();
      updateUser.setId(existingUser.getId());
      updateUser.setFailedCount(existingUser.getFailedCount()+1);
      updateByPrimaryKey(updateUser);
      throw new IncorrectPasswordException("登录失败，密码错误");
    }
    // 登录成功 清除失败次数
    else if (existingUser.getFailedCount() > 0) {
      UserDO updateUser = new UserDO();
      updateUser.setId(existingUser.getId());
      updateUser.setFailedCount(0);
      return updateByPrimaryKey(updateUser);
    }
    return existingUser;
  }
  
  @Override
  public Collection<RoleDO> addRoles(Integer userId, Collection<Integer> roleIds) {
    return null;
//    checkArgument(userId != null && userId >= 0, "userId: %d", userId);
//    checkArgument(roleIds != null && !roleIds.isEmpty(), "roleIds为空");
//    // 检查 id是否存在
//    checkNotFoundByPrimaryKey(userId);
//    checkRoleNotFoundById(roleIds);
//    Collection<UserRoleDO> userRoles = new ArrayList<>(roleIds.size());
//    roleIds.forEach(roleId -> {
//      var ur = new UserRoleDO();
//      ur.setRoleId(roleId);
//      ur.setUserId(userId);
//      userRoles.add(ur);
//    });
//    userRoleService.saveAll(userRoles);
//    return roleService.listByUserId(userId);
  }
  
  @Override
  public Collection<RoleDO> removeRoles(Integer userId, Collection<Integer> roleIds) {
    return null;
//    checkArgument(userId != null && userId >= 0, "userId: %d", userId);
//    checkArgument(roleIds != null && !roleIds.isEmpty(), "roleIds为空");
//    checkNotFoundByPrimaryKey(userId);
//    checkRoleNotFoundById(roleIds);
//    roleIds.forEach(roleId -> userRoleService.removeByPrimaryKey(roleId));
//    return roleService.listByUserId(userId);
  }
  
  /**
   * 如果指定的roleId不存在则抛出有异常
   * @param roleIds
   */
//  private void checkRoleNotFoundById(Collection<Integer> roleIds) {
//    for (Integer roleId : roleIds)
//      if (userRoleService.getByPrimaryKey(roleId).isEmpty())
//        throw new RoleNotFoundException("roleId: " + roleId);
//  }
  
  /**
   * 检查newPassword 并hash更新密码。
   * @param existingUser
   * @param newPassword
   * @return
   */
  private UserDO resetPassword0(UserDO existingUser, String newPassword) {
    checkPassword(newPassword);
    // 检查新旧密码是否相同
    if (passwordEncoder.matches(newPassword, existingUser.getHashPassword()))
      throw new IllegalArgumentException("新旧密码相同");
    UserDO updateUser = new UserDO();
    updateUser.setId(existingUser.getId());
    updateUser.setHashPassword(passwordEncoder.encode(newPassword));
    return updateByPrimaryKey(updateUser);
  }
  
  /**
   * 检查用户密码是否合法
   * @param password
   */
  private void checkPassword(String password) {
    if (StringUtils.isEmpty(password) || StringUtils.isBlank(password))
      throw new IllegalArgumentException("密码为空");
    if (password.trim().length() != password.length())
      throw new IllegalArgumentException("密码前后端存在空白符");
    final int minLen = 4, maxLen = 16;
    if (password.length() < minLen || password.length() > maxLen)
      throw new IllegalArgumentException("密码长度不合法，应该在 " + minLen + "," + maxLen);
    if (!password.chars().allMatch(ch -> CharUtils.isAscii((char)ch)))
      throw new IllegalArgumentException("密码中存在非法字符，不允许非ascii外的字符");
  }
  
  /**
   * 检查指定的安全码是否合法。如果正确则会溢出指定的安全码，否则抛出异常。该方法对于一个安全码仅调用一次
   * @param username
   * @param usedCode
   */
  private void checkSecureCode(SecureCodeService<String> secureCodeService, String address, String usedCode) {
    checkNotNull(usedCode);
    Optional<String> code = secureCodeService.getCode(address);
    // 重置码是否存在
    if (!code.isPresent())
      throw new SecureCodeNotFoundException("address: " + address);
    // 重置码是否正确
    if (!code.get().equals(usedCode))
      throw new IncorrectSecureCodeException("address: " + address + ", code: " + usedCode);
    // 移除重置码
    emailForgotPasswordService.removeCode(address);
  }
}
