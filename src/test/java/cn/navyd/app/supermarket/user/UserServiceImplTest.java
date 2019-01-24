package cn.navyd.app.supermarket.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import cn.navyd.app.supermarket.base.ReadOnlyDao;
import cn.navyd.app.supermarket.role.RoleDO;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock private UserDao userDao;
    @Mock private ReadOnlyDao<RoleDO> roleDao;
    private UserServiceImpl userService;
    
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userDao, roleDao);
        
        UserDO user = new UserDO();
        String username = "user";
        user.setUsername(username);
        
//        when(userDao.getByUsername(null)).thenThrow(NullPointerException.class);
//        when(userDao.getByUsername("user")).thenReturn(user);
//        when(userService.getByUsername("")).thenThrow(IllegalArgumentException.class);
//        when(userService.getByUsername(null)).thenThrow(NullPointerException.class);
    }
    
    @Test
    public void getByUsernameTest() {
        String username = "user";
//        UserDO user = new UserDO();
//        user.setUsername(username);
        
        when(userDao.getByUsername(isNull())).thenThrow(NullPointerException.class);
        when(userDao.getByUsername(username)).thenReturn(any());
//        assertThatThrownBy(() -> userDao.getByUsername(isNull())).isInstanceOf(NullPointerException.class);
        assertThat(userService.getByUsername(username).get().getUsername()).isEqualTo(username);
        verify(userDao, never()).getByUsername(isNull());
        verify(userDao).getByUsername(username);
//        verify(userDao, never()).getByUsername(isNull());
    }
    
    public static void main(String[] args) throws Exception {
    }
}
