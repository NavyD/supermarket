package cn.navyd.app.supermarket.role;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.util.PageUtil;

public class RoleDaoTest extends BaseDaoTest {
  @Autowired
  private RoleDao roleDao;
  // 测试数据
  private int id = 1;
  private int totalRows = 8;
  private int userId = 1;
  private int userRolesRows = 3;
  private String name = "用户";
  
  @Test
  void countTotalRowsTest() {
    assertThat(roleDao.countTotalRows())
      .isEqualTo(totalRows);
  }
  
  @Test
  void countRowsByLastIdTest() {
    final int lastId = 5, remainder = totalRows - lastId;
    assertThat(roleDao.countRowsByLastId(lastId))
      .isEqualTo(remainder);
  }
  
  @Test
  void getByPrimaryKeyTest() {
    assertThat(roleDao.getByPrimaryKey(id))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .matches(role -> role.getId() == id);
  }
  
  @Test
  void listPageTest() {
    int pageSize = 5, pageNum = 0;
    int expectedSize = PageUtil.calculateCurrentPageSize(totalRows, pageNum, pageSize);
    var roles = roleDao.listPage(pageNum, pageSize, null);
    assertThat(roles)
      .isNotNull()
      .isNotEmpty()
      .hasSize(expectedSize)
      .doesNotContainNull();
    
    pageSize = Integer.MAX_VALUE;
    pageNum = 0;
    expectedSize = PageUtil.calculateCurrentPageSize(totalRows, pageNum, pageSize);
    roles = roleDao.listPage(pageNum, pageSize, null);
    assertThat(roles)
      .isNotNull()
      .isNotEmpty()
      .hasSize(expectedSize)
      .doesNotContainNull();
  }
  
  @Transactional
  @Test
  void saveTest() {
    var role = getWholeRole();
    role.setId(null);
    
    roleDao.save(role);
    
    assertThat(role.getId()).isNotNull();
    
    var savedRole = roleDao.getByPrimaryKey(role.getId());
    assertThat(savedRole)
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(role, BASE_PROPERTIES);
  }
  
  @Transactional
  @Test
  void updateByPrimaryKeyTest() {
    assertThat(roleDao.getByPrimaryKey(id)).isNotNull();
    var role = getWholeRole();
    role.setId(id);
    
    roleDao.updateByPrimaryKey(role);
    
    var updatedRole = roleDao.getByPrimaryKey(id);
    assertThat(updatedRole)
      .isNotNull()
      .isEqualToIgnoringGivenFields(role, BASE_PROPERTIES);
  }
  
  @Transactional
  @Test
  void removeByPrimaryKeyTest() {
    assertThat(roleDao.getByPrimaryKey(id)).isNotNull();
    roleDao.removeByPrimaryKey(id);
    assertThat(roleDao.getByPrimaryKey(id)).isNull();
  }
  
  @Test
  void listByUserIdTest() {
    assertThat(roleDao.listByUserId(userId))
      .isNotNull()
      .isNotEmpty()
      .hasSize(userRolesRows)
      ;
  }
  
  @Test
  void getByNameTest() {
    assertThat(roleDao.getByName(name))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .matches(role -> role.getName().equals(name));
  }
  
  private RoleDO getWholeRole() {
    String name = getTestData("角色");
    boolean enabled = true;
    int id = Integer.MAX_VALUE;
    var now = LocalDateTime.now();
    var role = new RoleDO();
    role.setEnabled(enabled);
    role.setGmtCreate(now);
    role.setGmtModified(now);
    role.setId(id);
    role.setName(name);
    return role;
  }
}
