package cn.navyd.app.supermarket.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.AbstractBasicServiceTestData;
import cn.navyd.app.supermarket.BaseServiceTest;
import cn.navyd.app.supermarket.BasicServiceTestData;
import cn.navyd.app.supermarket.base.BaseService;
import cn.navyd.app.supermarket.base.ServiceException;
import cn.navyd.app.supermarket.user.UserNotFoundException;

public class RoleServiceImplTest extends BaseServiceTest<RoleDO> {
  @Autowired
  private RoleService roleService;
  
  @Test
  public void getByNameTest() {
    String name = data.getFirst().getName();
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
  public void listByUserIdTest() {
    int userId = 1;
    int roleSize = 3;
    assertThat(roleService.listByUserId(userId))
      .isNotNull()
      .hasSize(roleSize);
  }
  
  @Test
  public void listByUserIdTestForNotFoundException() {
    int userId = Integer.MAX_VALUE;
    assertThatThrownBy(() -> roleService.listByUserId(userId)).isInstanceOf(UserNotFoundException.class);
  }

  @Transactional
  @Test
  @Override
  public void saveTestForDuplicateException() {
    var savable = data.getSavable();
    savable.setName(data.getFirst().getName());
    assertThatThrownBy(() -> roleService.save(savable))
      .isInstanceOf(DuplicateRoleException.class);
  }

  @Override
  public void saveTestForNotFoundException() {
  }

  @Transactional
  @Test
  @Override
  public void saveTestForOtherException() {
    var savable = data.getSavable();
    savable.setName(data.getOverlengthString());
    assertThatThrownBy(() -> roleService.save(savable))
      .isInstanceOf(ServiceException.class);
  }

  @Transactional
  @Test
  @Override
  public void updateByPrimaryKeyTestForDuplicateException() {
    var savable = data.getSavable();
    savable.setId(data.getLastId());
    savable.setName(data.getFirst().getName());
    assertThatThrownBy(() -> roleService.updateByPrimaryKey(savable))
      .isInstanceOf(DuplicateRoleException.class);
  }

  @Transactional
  @Test
  @Override
  public void updateByPrimaryKeyTestForOtherException() {
    var updatable = data.getSavable();
    updatable.setId(data.getFirstId());
    updatable.setName(data.getOverlengthString());
    assertThatThrownBy(() -> roleService.updateByPrimaryKey(updatable))
      .isInstanceOf(ServiceException.class);
  }

  @Override
  protected BasicServiceTestData<RoleDO> getBasicServiceTestData() {
    return new AbstractBasicServiceTestData<RoleDO>(RoleTestData.getInstance()) {
      @Override
      public BaseService<RoleDO> getBaseService() {
        return roleService;
      }
    };
  }
  
}
