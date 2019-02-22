package cn.navyd.app.supermarket.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.base.ReadOnlyDao;
import cn.navyd.app.supermarket.user.UserDO;

public class RoleServiceImplTest extends BaseDaoTest {
  @Autowired
  private ReadOnlyDao<UserDO> userDao;
  @Autowired
  private RoleDao roleDao;
  private RoleServiceImpl roleService;
  private RoleDO role;
  
  @BeforeEach
  void setup() {
    roleService = new RoleServiceImpl(roleDao);
    roleService.setUserDao(userDao);
    int id = 1;
    this.role = roleService.getByPrimaryKey(id).get();
  }  
  
  @Test
  void getByPrimaryKeyTest() {
    int id = 1;
    assertThat(roleService.getByPrimaryKey(id))
      .isPresent()
      .get()
      .matches(r -> r.getId() == id);
  }
  
  @Transactional
  @Test
  void saveTest() {
    RoleDO saveRole = new RoleDO();
    saveRole.setEnabled(true);
    saveRole.setName(getTestData("rolename"));
    
    assertThat(roleService.save(saveRole))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(saveRole, BASE_PROPERTIES);
  }
  
  @Transactional
  @Test
  void saveDuplicateNameTest() {
    RoleDO saveRole = new RoleDO();
    saveRole.setEnabled(true);
    saveRole.setName(role.getName());
    assertThatThrownBy(() -> roleService.save(saveRole))
      .isInstanceOf(DuplicateRoleException.class);
  }
  
  @Test
  void getByNameTest() {
    String name = role.getName();
    assertThat(roleService.getByName(name))
      .isPresent()
      .get()
      .matches(r -> r.getName().equals(name));
    assertThat(roleService.getByName(getTestData("name"))).isEmpty();
    assertThatThrownBy(() -> roleService.getByName(null)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> roleService.getByName("")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> roleService.getByName(" ")).isInstanceOf(IllegalArgumentException.class);
  }
  
  @Test
  void listByUserIdTest() {
    int userId = 1;
    int roleSize = 3;
    assertThat(roleService.listByUserId(userId))
      .isNotNull()
      .hasSize(roleSize);
  }
  
}
