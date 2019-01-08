package cn.navyd.app.supermarket.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Random;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.BaseTest;
import cn.navyd.app.supermarket.util.SecurityUtils;

public class UserServiceTest extends BaseTest {
    private final Random rand = new Random();
    @Autowired
    private UserService userService;
    
    @Test
    public void getByPrimaryKeyTest() {
        int id = 11;
        var opUser = userService.getByPrimaryKey(id);
        assertTrue(opUser.isPresent());
    }
    
    @Test
    public void listPageTest() {
        int pageSize = 10;
        int pageNum = 0;
        var page = userService.listPage(pageSize, pageNum);
        assertEquals(pageSize, page.getPageSize());
        assertEquals(pageNum, page.getPageNumber());
        System.err.println(page);
    }
    
    @Transactional
    @Test
    public void saveTest() {
        var user = getWholeUserByRandom();
        assertNull(user.getId());
//        user.setEmail(null);
        var savedUser = userService.save(user);
    }
    
    @Transactional
    @Test
    public void updateByPrimaryKeyTest() {
        int id = 1;
        var existedUser = userService.getByPrimaryKey(id);
        assertNotNull(existedUser);
        
        var updateUser = getWholeUserByRandom();
        updateUser.setId(id);
        
        var updatedUser = userService.updateByPrimaryKey(updateUser);
        
    }
    
    @Transactional
    @Test
    public void removeByPrimaryKeyTest() {
        int id = 1;
        userService.removeByPrimaryKey(id);
        assertFalse(userService.getByPrimaryKey(id).isPresent());
    }
    
    /**
     * 获取完整随机属性的user对象。BaseDO中的属性不会设置
     * @return
     */
    private UserDO getWholeUserByRandom() {
        String username = "测试者_" + rand.nextInt(100000);
        String password = "" + rand.nextInt(100000);
        String email = rand.nextInt(1000000000) + "@a.com";
        int roleId = rand.nextInt(100);
        String phoneNumber = "" + rand.nextInt(100000000);
        String icon = "/" + rand.nextInt(100000000);
        var user = new UserDO();
        user.setEmail(email);
        user.setEnabled(rand.nextBoolean());
        user.setHashPassword(SecurityUtils.md5(password));
        user.setIconPath(icon);
        user.setPhoneNumber(phoneNumber);
        user.setRoleId(roleId);
        user.setUsername(username);
        return user;
    }
}
