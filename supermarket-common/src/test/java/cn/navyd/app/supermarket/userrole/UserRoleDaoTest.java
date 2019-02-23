package cn.navyd.app.supermarket.userrole;

import org.springframework.beans.factory.annotation.Autowired;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.base.BaseDao;

public class UserRoleDaoTest extends BaseDaoTest<UserRoleDO> {
  @Autowired
  private UserRoleDao userRoleDao;

  @Override
  protected BaseDao<UserRoleDO> getBaseDao() {
    return userRoleDao;
  }

  @Override
  protected UserRoleDO getFirst() {
    UserRoleDO userRole = new UserRoleDO();
    userRole.setRoleId(1);
    userRole.setUserId(1);
    return userRole;
  }

  @Override
  protected int getTotalRows() {
    return 3;
  }

  @Override
  protected UserRoleDO getSavable() {
    UserRoleDO userRole = new UserRoleDO();
    userRole.setRoleId(Integer.MAX_VALUE);
    userRole.setUserId(Integer.MAX_VALUE);
    return userRole;
  }
}
