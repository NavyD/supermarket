package cn.navyd.app.supermarket.user;

import java.util.Objects;
import java.util.Optional;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import cn.navyd.app.supermarket.base.AbstractBaseService;
import cn.navyd.app.supermarket.base.DuplicateException;
import cn.navyd.app.supermarket.base.NotFoundException;
import cn.navyd.app.supermarket.base.ReadOnlyDao;
import cn.navyd.app.supermarket.base.ServiceException;
import cn.navyd.app.supermarket.role.RoleDO;
import cn.navyd.app.supermarket.role.RoleNotFoundException;
import cn.navyd.app.supermarket.user.authentication.DisabledException;
import cn.navyd.app.supermarket.user.authentication.EmailRegisterService;
import cn.navyd.app.supermarket.user.authentication.IncorrectPasswordException;
import cn.navyd.app.supermarket.user.authentication.LockedException;
import cn.navyd.app.supermarket.user.authentication.LoginedUser;
import cn.navyd.app.supermarket.user.authentication.RegisterUserForm;
import cn.navyd.app.supermarket.user.reset.EmailForgotPasswordService;
import cn.navyd.app.supermarket.user.reset.OldPasswordUserForm;
import cn.navyd.app.supermarket.user.reset.SecureCodeUserForm;
import cn.navyd.app.supermarket.user.securecode.IncorrectSecureCodeException;
import cn.navyd.app.supermarket.user.securecode.SecureCodeNotFoundException;

@Service
public class UserServiceImpl extends AbstractBaseService<UserDao, UserDO> implements UserService {
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private EmailRegisterService emailRegisterService;
  @Autowired
  private EmailForgotPasswordService emailForgotPasswordService;
  private final ReadOnlyDao<RoleDO> roleDao;
  
  @Autowired
  public UserServiceImpl(UserDao dao, ReadOnlyDao<RoleDO> roleDao) {
    super(dao);
    this.roleDao = roleDao;
  }

  @Override
  protected void checkAssociativeNotFound(UserDO bean) throws NotFoundException {
    Integer roleId = bean.getRoleId();
    if (roleId == null)
      return;
    if (roleDao.getByPrimaryKey(roleId) == null)
      throw new RoleNotFoundException("id: " + roleId);
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
    UserDO user = dao.getByUsername(username);
    return Optional.ofNullable(user);
  }

  @Override
  public Optional<UserDO> getByEmail(String email) {
    Objects.requireNonNull(email);
    return Optional.ofNullable(dao.getByEmail(email));
  }

  @Override
  public UserDO register(RegisterUserForm registerUser) {
    Objects.requireNonNull(registerUser);
    // 检查username不存在
    final String username = registerUser.getUsername(), password = registerUser.getPassword(),
        email = registerUser.getEmail();
    Optional<UserDO> user = getByUsername(username);
    if (!user.isPresent())
      throw new UserNotFoundException("username: " + username);
    UserDO updateUser = new UserDO();
    updateUser.setUsername(username);
    updateUser.setEmail(email);
    updateUser.setPhoneNumber(registerUser.getPhoneNumber());
    updateUser.setIconPath(registerUser.getIconPath());
    // password加密
    checkPassword(password);
    String hashPassword = passwordEncoder.encode(password);
    updateUser.setHashPassword(hashPassword);
    // 设置不激活
    updateUser.setEnabled(false);
    // 配置默认的roleId
    final int defaultRoleId = 1; 
    updateUser.setRoleId(defaultRoleId);
    // 更新
    UserDO updatedUser = updateByPrimaryKey(updateUser);
    // 发送激活邮件
    emailRegisterService.sendCode(updatedUser);
    return updatedUser;
  }
  
  @Override
  public UserDO enableRegistration(Integer id, String registeredCode) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(registeredCode);
    // 检查user是否存在
    UserDO existingUser = checkNotFoundByPrimaryKey(id);
    // 检查user对应的code是否存在
    Optional<String> code = emailRegisterService.getCode(id);
    if (!code.isPresent())
      throw new SecureCodeNotFoundException("userId: " + id);
    // 注册码是否正确
    if (!code.get().equals(registeredCode))
      throw new IncorrectSecureCodeException("userId: " + id + ", code: " + registeredCode);
    if (existingUser.getEnabled())
      throw new ServiceException("用户账户已激活。user: " + existingUser);
    // 激活账户
    UserDO updateUser = new UserDO();
    updateUser.setId(id);
    updateUser.setEnabled(true);
    return updateByPrimaryKey(updateUser);
  }
  
  @Override
  public void forgotPassword(String email) {
    Objects.requireNonNull(email);
    // user是否存在
    Optional<UserDO> user = getByEmail(email);
    if (!user.isPresent())
      throw new UserNotFoundException("email: " + email);
    // 发送邮件
    emailForgotPasswordService.sendCode(user.get());
  }
  
  @Override
  public UserDO resetPassword(SecureCodeUserForm user) {
    Objects.requireNonNull(user);
    final Integer id = user.getId();
    final String resetCode = user.getCode();
    UserDO existingUser = checkNotFoundByPrimaryKey(id);
    Optional<String> code = emailForgotPasswordService.getCode(id);
    // 重置码是否存在
    if (!code.isPresent())
      throw new SecureCodeNotFoundException("userId: " + id);
    // 重置码是否正确
    if (!code.get().equals(resetCode))
      throw new IncorrectSecureCodeException("userId: " + id + ", code: " + resetCode);
    // 重置密码
    return resetPassword0(existingUser, user.getNewPassword());
  }
  
  @Override
  public UserDO resetPassword(OldPasswordUserForm user) {
    Objects.requireNonNull(user);
    final Integer id = user.getId();
    final String oldPassword = user.getOldPassword();
    UserDO existingUser = checkNotFoundByPrimaryKey(id);
    // 检查旧密码是否正确  多做了一次hash无所谓了bcrypt有点慢但好在重置功能少用
    String oldHashPassword = passwordEncoder.encode(oldPassword);
    if (!oldHashPassword.equals(existingUser.getHashPassword()))
      throw new IncorrectPasswordException();
    // 重置
    return resetPassword0(existingUser, user.getNewPassword());
  }
  
  /**
   * 用户登录操作
     * <ol>
     * <li>如果 未激活{@link UserDO#getEnabled()} 或 被锁定 则抛出异常
     * <li>如果用户密码错误则更新failCount并抛出异常
     * <li>如果登录成功则清除failCount并返回
     * </ol>
   */
  @Override
  public UserDO login(String username, String password) {
    Objects.requireNonNull(username);
    Objects.requireNonNull(password);
    Optional<UserDO> user = getByUsername(username);
    if (!user.isPresent())
      throw new UserNotFoundException("username: " + username);
    UserDO loginedUser = user.get();
    LoginedUser existingUser = new LoginedUser(loginedUser);
    // 账户未激活
    if (!existingUser.isEnabled())
      throw new DisabledException();
    // 账户已锁定
    if (existingUser.isLocked()) {
      throw new LockedException();
    }
    // 密码错误  设置登录失败次数
    if (!existingUser.getPassword().equals(password)) {
      UserDO updateUser = new UserDO();
      updateUser.setId(existingUser.getId());
      updateUser.setFailedCount(existingUser.getFailedCount()+1);
      updateByPrimaryKey(updateUser);
      throw new IncorrectPasswordException("username: " + username + ", password: " + password);
    }
    // 登录成功 清除失败次数
    else if (existingUser.getFailedCount() > 0) {
      UserDO updateUser = new UserDO();
      updateUser.setId(existingUser.getId());
      updateUser.setFailedCount(0);
      loginedUser = updateByPrimaryKey(updateUser);
    }
    return loginedUser;
  }
  
  /**
   * 使用newPassword重置密码
   * @param existingUser
   * @param newPassword
   * @return
   */
  private UserDO resetPassword0(UserDO existingUser, String newPassword) {
    // 更新密码
    checkPassword(newPassword);
    String newHashPassword = passwordEncoder.encode(newPassword);
    // 新旧密码不能相同
    if (newHashPassword.equals(existingUser.getHashPassword()))
      throw new IncorrectPasswordException("新旧密码不能相同");
    // 更新
    UserDO updateUser = new UserDO();
    updateUser.setId(existingUser.getId());
    updateUser.setHashPassword(newHashPassword);
    return updateByPrimaryKey(updateUser);
  }
  
  /**
   * 检查用户密码是否合法
   * @param password
   */
  private void checkPassword(String password) {
    if (Strings.isEmpty(password) || Strings.isBlank(password))
      throw new IncorrectPasswordException("密码格式错误。password: " + password);
  }
  
}
