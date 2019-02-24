package cn.navyd.app.supermarket.userrole;

import cn.navyd.app.supermarket.BasicTestData;

public class UserRoleTestData implements BasicTestData<UserRoleDO> {
  public static final UserRoleTestData INSTANCE = new UserRoleTestData();
  
  private UserRoleTestData() {
    
  }
  
  @Override
  public UserRoleDO getFirst() {
    UserRoleDO userRole = new UserRoleDO();
    userRole.setRoleId(1);
    userRole.setUserId(1);
    return userRole;
  }

  @Override
  public int getTotalRows() {
    return 3;
  }

  @Override
  public UserRoleDO getSavable() {
    UserRoleDO userRole = new UserRoleDO();
    userRole.setRoleId(1);
    userRole.setUserId(2);
    return userRole;
  }

}
