package cn.navyd.app.supermarket.userrole;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.util.PageUtils;

public class UserRoleDaoTest extends BaseDaoTest {
  @Autowired
  private UserRoleDao userRoleDao;
  
  private final int id = 1;
  private final int totalRows = 3;
  
  @Test
  void countTotalRowsTest() {
    assertThat(userRoleDao.countTotalRows()).isEqualTo(totalRows);
  }
  
  @Test
  void countRowsByLastIdTest() {
    final int lastId = 1, remainder = totalRows - lastId;
    assertThat(userRoleDao.countRowsByLastId(lastId))
      .isEqualTo(remainder);
  }
  
  @Test
  void getByPrimaryKeyTest() {
    assertThat(userRoleDao.getByPrimaryKey(id))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .matches(userRole -> userRole.getId() == id);
  }
  
  @Test
  void listPageTest() {
    int pageSize = 5, pageNum = 0;
    int expectedSize = PageUtils.getCurrentSize(totalRows, pageNum, pageSize);
    var roles = userRoleDao.listPage(pageNum, pageSize, null);
    assertThat(roles)
      .isNotNull()
      .isNotEmpty()
      .hasSize(expectedSize)
      .doesNotContainNull();
    
    pageSize = Integer.MAX_VALUE;
    pageNum = 0;
    expectedSize = PageUtils.getCurrentSize(totalRows, pageNum, pageSize);
    roles = userRoleDao.listPage(pageNum, pageSize, null);
    assertThat(roles)
      .isNotNull()
      .isNotEmpty()
      .hasSize(expectedSize)
      .doesNotContainNull();
  }
  
  @Transactional
  @Test
  void saveTest() {
    var userRole = getWholeUserRole();
    userRole.setId(null);
    
    userRoleDao.save(userRole);
    
    assertThat(userRole.getId()).isNotNull();
    assertThat(userRoleDao.getByPrimaryKey(userRole.getId()))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(userRole, BASE_PROPERTIES);
  }
  
  @Transactional
  @Test
  void updateByPrimaryKeyTest() {
    var existingUserRole = userRoleDao.getByPrimaryKey(id);
    assertThat(existingUserRole).isNotNull();
    var userRole = getWholeUserRole();
    userRole.setId(id);
    userRole.setGmtCreate(existingUserRole.getGmtCreate());
    
    userRoleDao.updateByPrimaryKey(userRole);
    
    assertThat(userRoleDao.getByPrimaryKey(id))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(userRole, BASE_PROPERTIES[2]);
  }
  
  @Transactional
  @Test
  void removeByPrimaryKeyTest() {
    assertThat(userRoleDao.getByPrimaryKey(id)).isNotNull();
    userRoleDao.removeByPrimaryKey(id);
    assertThat(userRoleDao.getByPrimaryKey(id)).isNull();
  }
  
  @Transactional
  @Test
  void saveAllTest() {
    final int userId = Integer.MAX_VALUE, roleId = Integer.MAX_VALUE;
    var userRole1 = new UserRoleDO();
    userRole1.setUserId(userId);
    userRole1.setRoleId(roleId);
    var userRole2 = new UserRoleDO();
    userRole2.setUserId(userId);
    userRole2.setRoleId(roleId - 1);
    var userRole3 = new UserRoleDO();
    userRole3.setUserId(userId-1);
    userRole3.setRoleId(roleId);
    userRoleDao.saveAll(Arrays.asList(userRole1, userRole2, userRole3));
    
    assertThat(userRole1.getId()).isNotNull();
    assertThat(userRole2.getId()).isNotNull();
    assertThat(userRole3.getId()).isNotNull();
    
    assertThat(userRoleDao.getByPrimaryKey(userRole1.getId()))
      .isNotNull()
      .isEqualToIgnoringGivenFields(userRole1, BASE_PROPERTIES[1], BASE_PROPERTIES[2]);
    assertThat(userRoleDao.getByPrimaryKey(userRole2.getId()))
      .isNotNull()
      .isEqualToIgnoringGivenFields(userRole2, BASE_PROPERTIES[1], BASE_PROPERTIES[2]);
    assertThat(userRoleDao.getByPrimaryKey(userRole3.getId()))
      .isNotNull()
      .isEqualToIgnoringGivenFields(userRole3, BASE_PROPERTIES[1], BASE_PROPERTIES[2]);
  }
  
  private UserRoleDO getWholeUserRole() {
    UserRoleDO userRole = new UserRoleDO();
    int id = Integer.MAX_VALUE, userId = Integer.MAX_VALUE, roleId = Integer.MAX_VALUE;
    var now = LocalDateTime.now();
    userRole.setId(id);
    userRole.setGmtCreate(now);
    userRole.setGmtModified(now);
    userRole.setRoleId(roleId);
    userRole.setUserId(userId);
    return userRole;
  }
}
