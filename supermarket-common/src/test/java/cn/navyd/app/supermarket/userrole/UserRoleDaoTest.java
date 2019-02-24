package cn.navyd.app.supermarket.userrole;

import org.springframework.beans.factory.annotation.Autowired;
import cn.navyd.app.supermarket.AbstractBasicDaoTestData;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.BasicDaoTestData;
import cn.navyd.app.supermarket.base.BaseDao;

public class UserRoleDaoTest extends BaseDaoTest<UserRoleDO> {
  @Autowired
  private UserRoleDao userRoleDao;
  
  @Override
  protected BasicDaoTestData<UserRoleDO> getBasicDaoTestData() {
    return new AbstractBasicDaoTestData<UserRoleDO>(UserRoleTestData.INSTANCE) {
      @Override
      public BaseDao<UserRoleDO> getBaseDao() {
        return userRoleDao;
      }
    };
  }
}
