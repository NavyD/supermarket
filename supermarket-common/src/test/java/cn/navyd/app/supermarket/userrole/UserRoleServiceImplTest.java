package cn.navyd.app.supermarket.userrole;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.AbstractBasicServiceTestData;
import cn.navyd.app.supermarket.BaseServiceTest;
import cn.navyd.app.supermarket.BasicServiceTestData;
import cn.navyd.app.supermarket.base.BaseService;
import cn.navyd.app.supermarket.role.RoleNotFoundException;
import cn.navyd.app.supermarket.user.UserNotFoundException;

public class UserRoleServiceImplTest extends BaseServiceTest<UserRoleDO> {
  @Autowired
  private UserRoleService userRoleService;
  
  @Transactional
  @Test
  public void updateByPrimaryKeyNotFoundExceptionTest() {
    final int userId = 1, roleId = 4;
    UserRoleDO updateUserRole = new UserRoleDO();
    updateUserRole.setId(Integer.MAX_VALUE);
    updateUserRole.setRoleId(roleId);
    updateUserRole.setUserId(userId);
    
    assertThatThrownBy(() -> userRoleService.updateByPrimaryKey(updateUserRole)).isInstanceOf(UserRoleNotFoundException.class);
  }

  @Transactional
  @Test
  @Override
  public void saveTestForDuplicateException() {
    var savable = data.getSavable();
    var first = data.getFirst();
    savable.setUserId(first.getUserId());
    savable.setRoleId(first.getRoleId());
    assertThatThrownBy(() -> userRoleService.save(savable)).isInstanceOf(DuplicateUserRoleException.class);
  }

  @Transactional
  @Test
  @Override
  public void saveTestForNotFoundException() {
    var savable = data.getSavable();
    var first = data.getFirst();
    savable.setUserId(Integer.MAX_VALUE);
    savable.setRoleId(first.getRoleId());
    assertThatThrownBy(() -> userRoleService.save(savable)).isInstanceOf(UserNotFoundException.class);
    
    
    savable.setUserId(first.getUserId());
    savable.setRoleId(Integer.MAX_VALUE);
    assertThatThrownBy(() -> userRoleService.save(savable)).isInstanceOf(RoleNotFoundException.class);
  }

  @Override
  public void saveTestForOtherException() {
  }
  
  @Transactional
  @Test
  @Override
  public void updateByPrimaryKeyTestForNotFoundException() {
    super.updateByPrimaryKeyTestForNotFoundException();
    
    var first = data.getFirst();
    var updatable = data.getSavable();
    updatable.setId(data.getLastId());
    updatable.setUserId(Integer.MAX_VALUE);
    updatable.setRoleId(first.getRoleId());
    assertThatThrownBy(() -> userRoleService.updateByPrimaryKey(updatable)).isInstanceOf(UserNotFoundException.class);
    
    updatable.setUserId(first.getUserId());
    updatable.setRoleId(Integer.MAX_VALUE);
    assertThatThrownBy(() -> userRoleService.updateByPrimaryKey(updatable)).isInstanceOf(RoleNotFoundException.class);
  }

  @Transactional
  @Test
  @Override
  public void updateByPrimaryKeyTestForDuplicateException() {
    var first = data.getFirst();
    var updatable = data.getSavable();
    updatable.setId(data.getLastId());
    updatable.setUserId(first.getUserId());
    updatable.setRoleId(first.getRoleId());
    assertThatThrownBy(() -> userRoleService.updateByPrimaryKey(updatable)).isInstanceOf(DuplicateUserRoleException.class);
  }

  @Override
  public void updateByPrimaryKeyTestForOtherException() {
    
  }

  @Override
  protected BasicServiceTestData<UserRoleDO> getBasicServiceTestData() {
    return new AbstractBasicServiceTestData<UserRoleDO>(UserRoleTestData.INSTANCE) {

      @Override
      public BaseService<UserRoleDO> getBaseService() {
        return userRoleService;
      }
      
    };
  }
  
}
