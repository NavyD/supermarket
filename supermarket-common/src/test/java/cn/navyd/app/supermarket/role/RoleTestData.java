package cn.navyd.app.supermarket.role;

import cn.navyd.app.supermarket.BaseTest;
import cn.navyd.app.supermarket.BasicTestData;

public class RoleTestData extends BaseTest implements BasicTestData<RoleDO> {
  private static final RoleTestData INSTANCE = new RoleTestData();
  
  public static RoleTestData getInstance() {
    return INSTANCE;
  }
  
  private RoleTestData() {
  }
  
  @Override
  public RoleDO getFirst() {
    var role = new RoleDO();
    role.setEnabled(true);
    role.setName("用户");
    return role;
  }

  @Override
  public int getTotalRows() {
    return 8;
  }

  @Override
  public RoleDO getSavable() {
    var role = new RoleDO();
    role.setEnabled(true);
    role.setName(getTestData("测试角色"));
    return role;
  }

}
