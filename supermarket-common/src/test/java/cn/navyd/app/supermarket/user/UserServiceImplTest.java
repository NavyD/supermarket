package cn.navyd.app.supermarket.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.BaseServiceTest;
import cn.navyd.app.supermarket.base.ServiceException;
import cn.navyd.app.supermarket.role.RoleDO;
import cn.navyd.app.supermarket.role.RoleNotFoundException;
import cn.navyd.app.supermarket.role.RoleService;
import cn.navyd.app.supermarket.user.authentication.DisabledException;
import cn.navyd.app.supermarket.user.authentication.IncorrectPasswordException;
import cn.navyd.app.supermarket.user.authentication.LockedException;
import cn.navyd.app.supermarket.user.authentication.RegisterUserForm;
import cn.navyd.app.supermarket.user.reset.OldPasswordUserForm;
import cn.navyd.app.supermarket.user.reset.SecureCodeUserForm;
import cn.navyd.app.supermarket.user.securecode.SecureCodeService;
import cn.navyd.app.supermarket.userrole.UserRoleService;
import cn.navyd.app.supermarket.util.PageInfo;
import cn.navyd.app.supermarket.util.PageUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest extends BaseServiceTest {
  private static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  @Autowired
  private UserDao userDao;
  @Autowired
  private UserRoleService userRoleService;
  @Autowired
  private RoleService roleService;
  @Mock
  private SecureCodeService emailRegisterService;
  @Mock
  private SecureCodeService emailForgotPasswordService;
  private String registerCode = "1234";
  private String forgotPasswordCode = "4231";
  private UserDO user;
  private UserDO secondUser;
  /** 对应user的原始密码*/
  private final String rawPassword = "1234"; 
  private UserServiceImpl userService;

  
  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
    final int id = 1, secondId = 2;
    this.user = userDao.getByPrimaryKey(id);
    this.secondUser = userDao.getByPrimaryKey(secondId);
    assertThat(user).isNotNull();
    assertThat(secondUser).isNotNull();
    
    userService = new UserServiceImpl(userDao);
    userService.setEmailForgotSecureCodeService(emailForgotPasswordService);
    userService.setEmailRegisterSecureCodeService(emailRegisterService);
    userService.setPasswordEncoder(PASSWORD_ENCODER);
    userService.setRoleService(roleService);
    userService.setUserRoleService(userRoleService);
  }
  
  @Test
  void listPageTest() {
    int totalRows = userDao.countTotalRows();
    int pageNumber = 0, pageSize = 10;
    PageInfo<UserDO> users = userService.listPage(pageNumber, pageSize);
    assertThat(users)
      .isNotNull()
      .matches(
          info -> info.getPageNumber() == pageNumber 
          && info.getPageSize() == pageSize
          && info.getTotalRows() == totalRows
          && info.getTotalPages() == PageUtils.getTotalPages(totalRows, pageSize)
          && info.getOffset() == PageUtils.getOffset(pageNumber, pageSize));
  }
  
  @Test
  void listPageExceptionTest() {
    final int pageNumber = Integer.MAX_VALUE, pageSize = 10;
    assertThatThrownBy(() -> userService.listPage(pageNumber, pageSize)).isInstanceOf(IllegalArgumentException.class);
  }
  
  @Test
  void listPageLastIdTest() {
    final int lastId = user.getId();
    int totalRows = userDao.countRowsByLastId(lastId);
    int pageNumber = 0, pageSize = 10;
    PageInfo<UserDO> users = userService.listPage(pageNumber, pageSize, lastId);
    assertThat(users)
      .isNotNull()
      .matches(
          info -> info.getPageNumber() == pageNumber 
          && info.getPageSize() == pageSize
          && info.getTotalRows() == totalRows
          && info.getTotalPages() == PageUtils.getTotalPages(totalRows, pageSize)
          && info.getOffset() == PageUtils.getOffset(pageNumber, pageSize));
  }
  
  @Test
  void getByEmailTest() {
    String email = user.getEmail();
    assertThat(userService.getByEmail(email))
      .isPresent()
      .get()
      .isEqualTo(user);
    assertThatThrownBy(() -> userService.getByEmail(null))
      .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> userService.getByEmail(""))
      .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> userService.getByEmail(" "))
      .isInstanceOf(IllegalArgumentException.class);
  }
  
  @Transactional
  @Test
  void saveTest() {
    String username = getTestData("测试用户");
    String password = "1234";
    String email = getTestData("email@aa.com");
    String phoneNumber = getTestData("00010");
    String icon = getTestData("/test/1");
    var user = new UserDO();
    user.setEmail(email);
    user.setEnabled(true);
    user.setHashPassword(PASSWORD_ENCODER.encode(password));
    user.setIconPath(icon);
    user.setPhoneNumber(phoneNumber);
    user.setUsername(username);
    user.setFailedCount(0);
    assertThat(user).hasNoNullFieldsOrPropertiesExcept(BASE_PROPERTIES);
    
    assertThat(userService.save(user))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(user, BASE_PROPERTIES);
  }
  
  @Transactional
  @Test
  void saveDuplicateUsernameTest() {
    String username = user.getUsername();
    String password = "1234";
    String email = getTestData("email@aa.com");
    String phoneNumber = getTestData("00010");
    String icon = getTestData("/test/1");
    var user = new UserDO();
    user.setEmail(email);
    user.setEnabled(true);
    user.setHashPassword(PASSWORD_ENCODER.encode(password));
    user.setIconPath(icon);
    user.setPhoneNumber(phoneNumber);
    user.setUsername(username);
    user.setFailedCount(0);
    
    assertThatThrownBy(() -> userService.save(user)).isInstanceOf(DuplicateUserException.class);
  }
  
  @Transactional
  @Test
  void saveOtherExceptionTest() {
    String username = getTestData("user");
    String password = "1234";
    String email = null;
    String phoneNumber = getTestData("00010");
    String icon = getTestData("/test/1");
    var user = new UserDO();
    user.setEmail(email);
    user.setEnabled(true);
    user.setHashPassword(PASSWORD_ENCODER.encode(password));
    user.setIconPath(icon);
    user.setPhoneNumber(phoneNumber);
    user.setUsername(username);
    user.setFailedCount(0);
    
    assertThatThrownBy(() -> 
    userService.save(user)).isInstanceOf(ServiceException.class);
  }
  
  @Transactional
  @Test
  void updateByPrimaryKeyTest() {
    var updateUser = getUpdateUser();
    updateUser.setId(user.getId());
    String[] ignoredProperties = {BASE_PROPERTIES[1], BASE_PROPERTIES[2]};
    assertThat(updateUser).hasNoNullFieldsOrPropertiesExcept(ignoredProperties);
    assertThat(userService.updateByPrimaryKey(updateUser))
      .isNotNull()
      .isEqualToIgnoringGivenFields(updateUser, ignoredProperties);
  }
  
  @Transactional
  @Test
  void updateByPrimaryKeyExceptionTest() {
    var updateUser = getUpdateUser();
    updateUser.setId(user.getId());
    String[] ignoredProperties = {BASE_PROPERTIES[1], BASE_PROPERTIES[2]};
    assertThat(updateUser).hasNoNullFieldsOrPropertiesExcept(ignoredProperties);
    
    // username duplicate
    updateUser.setUsername(secondUser.getUsername());
    assertThatThrownBy(() -> userService.updateByPrimaryKey(updateUser)).isInstanceOf(DuplicateUserException.class);
    
    // id not found
    updateUser.setUsername(getTestData("user"));
    updateUser.setId(Integer.MAX_VALUE);
    assertThatThrownBy(() -> userService.updateByPrimaryKey(updateUser)).isInstanceOf(UserNotFoundException.class);
  }
  
  @Transactional
  @Test
  void removeByPrimaryKeyTest() {
    int id = user.getId();
    userService.removeByPrimaryKey(id);
    assertThat(userService.getByPrimaryKey(id)).isEmpty();
  }

  @Transactional
  @Test
  void registerTest() {
    RegisterUserForm user = new RegisterUserForm();
    user.setEmail("aabb@email.com");
    user.setUsername(getTestData("user"));
    user.setPassword("password");
    user.setPhoneNumber("13344445555");
    user.setIconPath("/a/b");
    user.setCode(registerCode);
    
    when(emailRegisterService.getCode(user.getEmail()))
      .thenReturn(Optional.of(registerCode));
    var registeredUser = userService.register(user);
    // 两者忽略的属性 
    String[] ignoredProperties = {"id", "gmtCreate", "gmtModified", "password", "hashPassword", "enabled", "failedCount"};
    assertThat(registeredUser)
      .isNotNull()
      .isEqualToIgnoringGivenFields(user, ignoredProperties)
      .matches(u -> u.getEnabled(), "已激活");
  }
  
  @Transactional
  @Test
  void registerDuplicateUsernameTest() {
    RegisterUserForm userForm = new RegisterUserForm();
    // 重复的username
    userForm.setUsername(user.getUsername());
    userForm.setEmail("aabb@email.com");
    userForm.setPassword("password");
    userForm.setPhoneNumber("13344445555");
    userForm.setIconPath("/a/b");
    userForm.setCode(registerCode);
    
    assertThatThrownBy(() -> userService.register(userForm))
      .isInstanceOf(DuplicateUserException.class);
  }
  
  @Transactional
  @Test
  void registerDuplicateEmailTest() {
    RegisterUserForm userForm = new RegisterUserForm();
    // 重复的username
    userForm.setUsername(getTestData("user"));
    userForm.setEmail(user.getEmail());
    userForm.setPassword("password");
    userForm.setPhoneNumber("13344445555");
    userForm.setIconPath("/a/b");
    userForm.setCode(registerCode);
    
    assertThatThrownBy(() -> userService.register(userForm))
      .isInstanceOf(DuplicateUserException.class);
  }
  
  @Test
  void sendRegisteringCodeByEmail() {
    String email = "232@qq.com";
    userService.sendRegisteringCodeByEmail(email);
    verify(emailRegisterService).sendCode(email);
    
    // 已存在的email
    assertThatThrownBy(() -> userService.sendRegisteringCodeByEmail(user.getEmail()))
      .isInstanceOf(DuplicateUserException.class);
  }

  @Test
  void sendForgotPasswordCodeByEmailTest() {
    String email = user.getEmail();
    userService.sendForgotPasswordCodeByEmail(email);
    verify(emailForgotPasswordService).sendCode(email);
    
    // 未知的email
    assertThatThrownBy(() -> userService.sendForgotPasswordCodeByEmail(getTestData("aa@aa.com")))
      .isInstanceOf(UserNotFoundException.class);
  }
  
  @Transactional
  @Test
  void resetPasswordWithCodeTest() {
    SecureCodeUserForm userForm = new SecureCodeUserForm();
    userForm.setCode(forgotPasswordCode);
    userForm.setId(user.getId());
    userForm.setNewPassword("1234321");
    
    when(emailForgotPasswordService.getCode(user.getEmail()))
      .thenReturn(Optional.of(forgotPasswordCode));
    
    var resetedUser = userService.resetPassword(userForm);
    assertThat(resetedUser)
      .isNotNull()
      .isEqualToIgnoringGivenFields(user, "hashPassword", BASE_PROPERTIES[2])
      .matches(
          u -> !u.getHashPassword().equals(user.getHashPassword()) 
          && PASSWORD_ENCODER.matches(userForm.getNewPassword(), u.getHashPassword()), 
          "更新后的密码成功");
  }
  
  @Transactional
  @Test
  void resetPasswordWithOldPasswordTest() {
    OldPasswordUserForm userForm = new OldPasswordUserForm();
    userForm.setId(user.getId());
    userForm.setNewPassword("1234321");
    userForm.setOldPassword(rawPassword);
    
    var resetedUser = userService.resetPassword(userForm);
    assertThat(resetedUser)
      .isNotNull()
      .matches(
          u -> PASSWORD_ENCODER.matches(userForm.getNewPassword(), resetedUser.getHashPassword())
          && !u.getHashPassword().equals(user.getHashPassword()),
          "更新密码成功");
  }
  
  @Transactional
  @Test
  void resetPasswordWithOldPasswordFailedTest() {
    OldPasswordUserForm userForm = new OldPasswordUserForm();
    String rawPasswordFailed = getTestData("passw");
    userForm.setId(user.getId());
    userForm.setNewPassword("1234321");
    userForm.setOldPassword(rawPasswordFailed);
    assertThatThrownBy(() -> userService.resetPassword(userForm)).isInstanceOf(IncorrectPasswordException.class);
    
    // 新旧密码相同
    userForm.setNewPassword(rawPassword);
    userForm.setOldPassword(rawPassword);
    assertThatThrownBy(() -> userService.resetPassword(userForm)).isInstanceOf(IllegalArgumentException.class);
  }
  
  @Transactional
  @Test
  void loginTest() {
    String username = user.getUsername(), rawPassword = this.rawPassword;
    var loginedUser = userService.login(username, rawPassword);
    assertThat(loginedUser)
      .isNotNull()
      .matches(u -> u.getUsername().equals(user.getUsername()) && u.getFailedCount() == 0);
  }
  
  @Transactional
  @Test
  void loginFailedCountTest() {
    final String username = user.getUsername(), rawPasswordFailed = "asdfadsfas";
    final int failedLoop = 5;
    for (int i = 0; i < failedLoop; i++) {
      assertThatThrownBy(() -> userService.login(username, rawPasswordFailed))
        .isInstanceOf(IncorrectPasswordException.class);
      final int times = i;
      assertThat(userService.getByUsername(username))
        .isNotNull()
        .matches(u -> u.get().getFailedCount()-1 == times);
    }
    
    // 错误登录次数过多，锁定
    assertThatThrownBy(() -> userService.login(username, rawPasswordFailed))
      .isInstanceOf(LockedException.class);
  }
  
  @Transactional
  @Test
  void loginPasswordFailedThenSuccessfulTest() {
    final String username = user.getUsername(), rawPasswordFailed = "asdfadsfas";
    assertThatThrownBy(() -> userService.login(username, rawPasswordFailed))
      .isInstanceOf(IncorrectPasswordException.class);
    
    assertThat(userService.getByUsername(username))
      .isNotNull()
      .matches(u -> u.get().getFailedCount() == 1);
      
    var loginedUser = userService.login(username, rawPassword);
    assertThat(loginedUser)
      .isNotNull()
      .matches(u -> u.getFailedCount() == 0);
  }
  
  @Test
  void loginUsernameFailedThenSuccessfulTest() {
    final String username = getTestData("username");
    assertThatThrownBy(() -> userService.login(username, rawPassword))
      .isInstanceOf(UserNotFoundException.class);
  }
  
  @Transactional
  @Test
  void loginUnenabledTest() {
    var updateUneabledUser = new UserDO();
    updateUneabledUser.setId(user.getId());
    updateUneabledUser.setEnabled(false);
    
    userService.updateByPrimaryKey(updateUneabledUser);
    assertThatThrownBy(() -> userService.login(user.getUsername(), rawPassword))
      .isInstanceOf(DisabledException.class);
  }
  
  @SuppressWarnings("unchecked")
  @Transactional
  @Test
  void addRolesTest() {
    int userId = user.getId();
    Collection<Integer> roleIds = Arrays.asList(4, 5);
    assertThat(userService.addRoles(userId, roleIds))
      .isNotNull()
      .matches(roles -> ((Collection<RoleDO>) roles).size() > roleIds.size())
      .areAtLeast(2, new Condition<>(r -> roleIds.contains(r.getId()), "roleIds:%s", roleIds));
  }
  
  @Transactional
  @Test
  void addRolesNotFoundRoleTest() {
    int userId = user.getId();
    Collection<Integer> roleIds = Arrays.asList(Integer.MAX_VALUE, 5);
    assertThatThrownBy(() -> userService.addRoles(userId, roleIds)).isInstanceOf(RoleNotFoundException.class);
  }
  
  @Transactional
  @Test
  void removeRolesTest() {
    int roleId = 1, userId = user.getId();
    assertThat(userService.removeRoles(userId, roleId))
      .isNotNull()
      .allMatch(r -> r.getId() != roleId);
  }
  
  UserDO getUpdateUser() {
    String password = "1234";
    var updateUser = new UserDO();
    updateUser.setId(user.getId());
    updateUser.setEmail(getTestData("aa@email.com"));
    updateUser.setEnabled(true);
    updateUser.setHashPassword(PASSWORD_ENCODER.encode(password));
    updateUser.setIconPath(getTestData("/test/1"));
    updateUser.setPhoneNumber(getTestData("00010"));
    updateUser.setUsername(getTestData("user"));
    updateUser.setFailedCount(0);
    assertThat(updateUser).hasNoNullFieldsOrPropertiesExcept(BASE_PROPERTIES[1], BASE_PROPERTIES[2]);
    return updateUser;
  }
}
