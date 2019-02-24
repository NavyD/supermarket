package cn.navyd.app.supermarket.role;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import cn.navyd.app.supermarket.AbstractBasicDaoTestData;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.BasicDaoTestData;
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
    String name = data.getFirst().getName();
    assertThat(roleDao.getByName(name))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .matches(role -> role.getName().equals(name));
  }
  
  @Override
  protected BasicDaoTestData<RoleDO> getBasicDaoTestData() {
    return new AbstractBasicDaoTestData<RoleDO>(RoleTestData.getInstance()) {
      @Override
      public BaseDao<RoleDO> getBaseDao() {
        return roleDao;
      }
    };
  }
}
