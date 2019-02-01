package cn.navyd.app.supermarket.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import cn.navyd.app.supermarket.BaseMockTest;
import cn.navyd.app.supermarket.base.ReadOnlyDao;
import cn.navyd.app.supermarket.role.RoleDO;
import cn.navyd.app.supermarket.user.authentication.EmailRegisterService;
import cn.navyd.app.supermarket.user.reset.EmailForgotPasswordService;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest extends BaseMockTest {
    @Mock private UserDao mockUserDao;
    @Mock private ReadOnlyDao<RoleDO> roleDao;
    @Mock private EmailRegisterService emailRegisterService;
    @Mock private EmailForgotPasswordService emailForgotPasswordService;
    private String registerCode = "1234";
    private String forgotPasswordCode = "4231";
    private UserDO user;
    private UserServiceImpl userService;
    
    @BeforeEach
    public void before() {
      MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(mockUserDao, roleDao, emailRegisterService, emailForgotPasswordService);
        user = getWholeUser();
        
        when(mockUserDao.getByEmail(user.getEmail())).thenReturn(user);
        when(mockUserDao.getByEmail(isNull())).thenThrow(NullPointerException.class);
        when(emailRegisterService.getCode(user.getId())).thenReturn(Optional.of(registerCode));
//        when(userDao.getByUsername(null)).thenThrow(NullPointerException.class);
//        when(userDao.getByUsername("user")).thenReturn(user);
//        when(userService.getByUsername("")).thenThrow(IllegalArgumentException.class);
//        when(userService.getByUsername(null)).thenThrow(NullPointerException.class);
    }
    
    @Test
    void getByEmailTest() {
      String email = user.getEmail();
      userService.getByEmail(email);
      verify(mockUserDao).getByEmail(email);
    }
    
    @Test
//    public void getByUsernameTest() {
//        String username = "user";
////        UserDO user = new UserDO();
////        user.setUsername(username);
//        
//        when(userDao.getByUsername(isNull())).thenThrow(NullPointerException.class);
//        when(userDao.getByUsername(username)).thenReturn(any());
////        assertThatThrownBy(() -> userDao.getByUsername(isNull())).isInstanceOf(NullPointerException.class);
//        assertThat(userService.getByUsername(username).get().getUsername()).isEqualTo(username);
//        verify(userDao, never()).getByUsername(isNull());
//        verify(userDao).getByUsername(username);
////        verify(userDao, never()).getByUsername(isNull());
//    }
    
    private UserDO getWholeUser() {
      String username = getTestData("测试用户");
      String password = getTestData("1234");
      String email = getTestData("email@aa.com");
      String phoneNumber = getTestData("00010");
      String icon = getTestData("/test/1");
      boolean enabled = true;
      int failedCount = 1;
      int roleId = Integer.MAX_VALUE;
      int id = Integer.MAX_VALUE;
      LocalDateTime now = LocalDateTime.now();
      var user = new UserDO();
      user.setId(id);
      user.setEmail(email);
      user.setEnabled(enabled);
      user.setHashPassword(password);
      user.setIconPath(icon);
      user.setPhoneNumber(phoneNumber);
      user.setRoleId(roleId);
      user.setUsername(username);
      user.setEnabled(enabled);
      user.setFailedCount(failedCount);
      user.setGmtCreate(now);
      user.setGmtModified(now);
      return user;
    }
}
