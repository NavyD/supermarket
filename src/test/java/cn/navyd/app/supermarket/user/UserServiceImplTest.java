package cn.navyd.app.supermarket.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.BaseMockTest;
import cn.navyd.app.supermarket.role.RoleService;
import cn.navyd.app.supermarket.user.authentication.EmailRegisterService;
import cn.navyd.app.supermarket.user.authentication.RegisterUserForm;
import cn.navyd.app.supermarket.user.reset.EmailForgotPasswordService;
import cn.navyd.app.supermarket.user.reset.SecureCodeUserForm;

public class UserServiceImplTest extends BaseMockTest {
  private static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  @Autowired
  private UserDao userDao;
  @Mock
  private RoleService roleService;
  @Mock
  private EmailRegisterService emailRegisterService;
  @Mock
  private EmailForgotPasswordService emailForgotPasswordService;
  private String registerCode = "1234";
  private String forgotPasswordCode = "4231";
  private UserDO user;
  private UserServiceImpl userService;

//  @BeforeEach
  public void before() {
    userService = new UserServiceImpl(userDao);
    userService.setEmailForgotPasswordService(emailForgotPasswordService);
    userService.setEmailRegisterService(emailRegisterService);
    userService.setPasswordEncoder(PASSWORD_ENCODER);
    userService.setRoleService(roleService);
    
    when(emailRegisterService.getCode(user.getUsername())).thenReturn(Optional.of(registerCode));
    // when(userDao.getByUsername(null)).thenThrow(NullPointerException.class);
    // when(userDao.getByUsername("user")).thenReturn(user);
    // when(userService.getByUsername("")).thenThrow(IllegalArgumentException.class);
    // when(userService.getByUsername(null)).thenThrow(NullPointerException.class);
  }
  
  @BeforeEach
  void setup() {
    int id = 1;
    var user = userDao.getByPrimaryKey(id);
    assertThat(user).isNotNull();
    this.user = user;
    
    userService = new UserServiceImpl(userDao);
    userService.setEmailForgotPasswordService(emailForgotPasswordService);
    userService.setEmailRegisterService(emailRegisterService);
    userService.setPasswordEncoder(PASSWORD_ENCODER);
    userService.setRoleService(roleService);
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
  void registerTest() {
    RegisterUserForm user = new RegisterUserForm();
    user.setEmail("aabb@email.com");
    user.setUsername("user");
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
  
  @Test
  void sendRegisteringCodeByEmail() {
    String email = "232@qq.com";
    userService.sendRegisteringCodeByEmail(email);
    verify(emailRegisterService).sendCode(email);
    
    // 已存在的email
    assertThatThrownBy(() -> userService.sendRegisteringCodeByEmail(user.getEmail()))
      .isInstanceOf(DuplicateUserException.class);
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
      .matches(u -> !u.getHashPassword().equals(user.getHashPassword()), "更新后的密码成功");
  }
  
//   private UserDO getWholeUser() {
//     String username = getTestData("测试用户");
//     String password = getTestData("1234");
//     String email = getTestData("email@aa.com");
//     String phoneNumber = getTestData("00010");
//     String icon = getTestData("/test/1");
//     boolean enabled = true;
//     int failedCount = 1;
//     int id = Integer.MAX_VALUE;
//     LocalDateTime now = LocalDateTime.now();
//     var user = new UserDO();
//     user.setId(id);
//     user.setEmail(email);
//     user.setEnabled(enabled);
//     user.setHashPassword(password);
//     user.setIconPath(icon);
//     user.setPhoneNumber(phoneNumber);
//     user.setUsername(username);
//     user.setEnabled(enabled);
//     user.setFailedCount(failedCount);
//     user.setGmtCreate(now);
//     user.setGmtModified(now);
//     return user;
//   }
}
