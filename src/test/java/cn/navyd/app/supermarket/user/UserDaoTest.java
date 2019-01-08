package cn.navyd.app.supermarket.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.BaseTest;
import cn.navyd.app.supermarket.util.SecurityUtils;

public class UserDaoTest extends BaseTest {
  @Autowired
  private UserDao userDao;
  private final Random rand = new Random();
  private UserDO firstUser;
  
  @BeforeEach
  void firstUser() {
    int id = 1; 
    var user = userDao.getByPrimaryKey(id);
    assertThat(user)
      .isNotNull()
      .matches(u -> u.getId() == id);
    this.firstUser = user;
  }

  @Test
  public void getByPrimaryKeyBaseTest() {
    final int id = 1;
    var user = userDao.getByPrimaryKey(id);
    assertThat(user).isNotNull().hasNoNullFieldsOrPropertiesExcept(GET_USERDO_NULLABLE_PROPERTIES)
        .matches(u -> u.getId() == id);

    final int invalidId = rand.nextInt() * -1;
    user = userDao.getByPrimaryKey(invalidId);
    assertThat(user).isNull();
  }

  @Test
  public void listPageTest() {
    int pageSize = 10;
    int pageNum = 0;
    var users = userDao.listPage(pageNum, pageSize, null);
    assertThat(users)
      .isNotNull()
      .isNotEmpty()
      .doesNotContainNull()
      .hasSize(pageSize)
      .startsWith(firstUser);

    // pageNum < 0
    pageNum = -1;
    pageSize = 2;
    users = userDao.listPage(pageNum, pageSize, null);
    assertThat(users)
      .isNotNull()
      .doesNotContainNull()
      .hasSize(pageSize);
  }

  @Test
  public void countTotalRowsTest() {
    int count = userDao.countTotalRows();
    System.err.println(count);
    // assertEquals(userDao.listAll().size(), count);
  }
  
  @Transactional
  @Test
  public void saveBaseTest() {
    var user = getUserByRandom();
    assertThat(user)
      .isNotNull()
      .matches(u -> u.getId()==null);

    userDao.save(user);

    var id = user.getId();
    var savedUser = userDao.getByPrimaryKey(id);
    assertThat(savedUser)
      .isNotNull()
      .hasNoNullFieldsOrPropertiesExcept(GET_USERDO_NULLABLE_PROPERTIES)
      .matches(u ->{
        return u.getId() == savedUser.getId(); 
      })
      ;
  }

  @Transactional
  @Test
  public void updateByPrimaryKeyTest() {
    int id = 2;
    var existedUser = userDao.getByPrimaryKey(id);
    assertNotNull(existedUser);

    var updateUser = getWholeUserByRandom();
    updateUser.setId(id);

    userDao.updateByPrimaryKey(updateUser);

    var updatedUser = userDao.getByPrimaryKey(id);
    assertThat(updatedUser)
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .matches(user -> user.getId().equals(id));
  }

  @Transactional
  @Test
  public void removeByPrimaryKeyTest() {
    int id = 2;
    assertThat(userDao.getByPrimaryKey(id))
      .isNotNull()
      .matches(user -> user.getId().equals(id));
    userDao.removeByPrimaryKey(id);
    assertThat(userDao.getByPrimaryKey(id)).isNull();
  }

  // 完整性检查。插入完整的user获取是否完整
  @Transactional
  @Test
  public void getByPrimaryKeyWholeTest() {
    var user = getWholeUserByRandom();
    assertThat(user)
      .isNotNull()
      .hasNoNullFieldsOrPropertiesExcept(BASEDO_PROPERTIES)
      .matches(u -> u.getId() == null);

    userDao.save(user);

    assertNotNull(user.getId());
    var id = user.getId();

    var savedUser = userDao.getByPrimaryKey(id);

    assertNotNull(savedUser);
  }

  @Transactional
  @Test
  public void getByPrimaryKeyCacheTest() {
    int id = 2;
    var user = userDao.getByPrimaryKey(id);
    assertNotNull(user);
    String formerUsername = user.getUsername();
    // 修改 username 将会导致 局部缓存被修改
    user.setUsername("" + rand.nextInt(100000));
    assertNotEquals(formerUsername, user.getUsername());
    var cachedUser = userDao.getByPrimaryKey(id);
    // 缓存被修改
    assertNotEquals(formerUsername, cachedUser.getUsername());
  }

  private UserDO getUserByRandom() {
    String username = "测试者_" + rand.nextInt(100000);
    String password = "1234";
    String email = rand.nextInt(1000000000) + "@a.com";
    int roleId = rand.nextInt(100);
    return UserDO.ofSavable(username, password, email, roleId);
  }

  /**
   * 获取完整随机属性的user对象。BaseDO中的属性不会设置
   * 
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
