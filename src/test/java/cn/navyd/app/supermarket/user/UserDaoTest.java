package cn.navyd.app.supermarket.user;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.util.PageUtil;

public class UserDaoTest extends BaseDaoTest {
  @Autowired
  private UserDao userDao;
  static final String[] NULLABLE_PROPERTIES = {"iconPath", "phoneNumber"};
  // 测试数据 默认
  private final int id = 1;
  private final String username = "测试用户";
  private final String email = "aabb@cc.dd";
  private final int totalRows = 1;
  
  @Test
  public void getByPrimaryKeyBaseTest() {
    var user = userDao.getByPrimaryKey(id);
    assertThat(user).isNotNull()
      .matches(u -> u.getId() == id)
      .hasNoNullFieldsOrPropertiesExcept(NULLABLE_PROPERTIES);

    int invalidId = -1;
    user = userDao.getByPrimaryKey(invalidId);
    assertThat(user).isNull();
    
    invalidId = Integer.MAX_VALUE;
    user = userDao.getByPrimaryKey(invalidId);
    assertThat(user).isNull();
  }
  
  @Test
  void getByUsernameTest() {
    assertThat(userDao.getByUsername(username))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .matches(u -> u.getUsername().equals(username));
  }
  
  @Test
  void getByEmailTest() {
    assertThat(userDao.getByEmail(email))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .matches(u -> u.getEmail().equals(email));
  }
  
  @Test
  public void countTotalRowsTest() {
    assertThat(userDao.countTotalRows()).matches(count -> count == totalRows);
  }
  
  @Test
  public void countRowsByLastIdTest() {
    // 测试数据为连续的
    final int lastId = 1, remainder = totalRows - lastId;
    assertThat(userDao.countRowsByLastId(lastId)).matches(count -> count == remainder);
  }

  @Test
  public void listPageTest() {
    int pageSize = 5;
    int pageNum = 0;
    int expectedSize = PageUtil.calculateCurrentPageSize(totalRows, pageNum, pageSize);
    var users = userDao.listPage(pageNum, pageSize, null);
    assertThat(users)
      .isNotNull()
      .isNotEmpty()
      .hasSize(expectedSize)
      .doesNotContainNull();

    pageSize = Integer.MAX_VALUE;
    pageNum = 0;
    expectedSize = PageUtil.calculateCurrentPageSize(totalRows, pageNum, pageSize);
    users = userDao.listPage(pageNum, pageSize, null);
    assertThat(users)
      .isNotNull()
      .isNotEmpty()
      .hasSize(expectedSize)
      .doesNotContainNull();
  }
  
  @Transactional
  @Test
  public void saveBaseTest() {
    var user = getWholeUser();
    assertThat(user)
      .isNotNull()
      .hasNoNullFieldsOrProperties();
    user.setId(null);
    
    userDao.save(user);

    var id = user.getId();
    var savedUser = userDao.getByPrimaryKey(id);
    
    assertThat(savedUser)
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(user, BASE_PROPERTIES);
  }

  @Transactional
  @Test
  public void updateByPrimaryKeyTest() {
    var existingUser = userDao.getByPrimaryKey(id);
    assertThat(existingUser).isNotNull();

    var updateUser = getWholeUser();
    updateUser.setId(id);
    updateUser.setGmtCreate(existingUser.getGmtCreate());

    userDao.updateByPrimaryKey(updateUser);

    assertThat(userDao.getByPrimaryKey(id))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(updateUser, BASE_PROPERTIES[2]);
  }

  @Transactional
  @Test
  public void removeByPrimaryKeyTest() {
    assertThat(userDao.getByPrimaryKey(id)).isNotNull();
    userDao.removeByPrimaryKey(id);
    assertThat(userDao.getByPrimaryKey(id)).isNull();
  }

  @Transactional
  @Test
  public void getByPrimaryKeyCacheTest() {
    var user = userDao.getByPrimaryKey(id);
    assertThat(user).isNotNull();
    // 修改 username 将会导致 局部缓存被修改
    String newUsername = getTestData("测试");
    user.setUsername(newUsername);
    // 缓存被修改
    assertThat(userDao.getByPrimaryKey(id))
      .matches(u -> u.getUsername().equals(newUsername));
  }

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
