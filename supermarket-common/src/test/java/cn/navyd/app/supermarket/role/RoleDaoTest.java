package cn.navyd.app.supermarket.role;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.base.BaseDao;

public class RoleDaoTest extends BaseDaoTest<RoleDO> {
  @Autowired
  private RoleDao roleDao;
  
  @Test
  void listByUserIdTest() {
    // 测试数据
    final int userId = 1, userRolesRows = 3;
    assertThat(roleDao.listByUserId(userId))
      .isNotNull()
      .isNotEmpty()
      .hasSize(userRolesRows);
  }
  
  @Test
  void getByNameTest() {
    String name = getFirst().getName();
    assertThat(roleDao.getByName(name))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .matches(role -> role.getName().equals(name));
  }

  @Override
  protected BaseDao<RoleDO> getBaseDao() {
    return roleDao;
  }

  @Override
  protected RoleDO getFirst() {
    var role = new RoleDO();
    role.setEnabled(true);
    role.setName("用户");
    return role;
  }

  @Override
  protected int getTotalRows() {
    return 8;
  }

  @Override
  protected RoleDO getSavable() {
    var role = new RoleDO();
    role.setEnabled(true);
    role.setName(getTestData("测试角色"));
    return role;
  }
}
