package cn.navyd.app.supermarket.user;

import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import cn.navyd.app.supermarket.BaseServiceTest;

public class UserServiceTest extends BaseServiceTest {
    @Autowired
    private UserService userService;
    
    @Test
    public void getByPrimaryKeyTest() {
        int id = 1;
        var opUser = userService.getByPrimaryKey(id);
        assertTrue(opUser.isPresent());
    }
}
