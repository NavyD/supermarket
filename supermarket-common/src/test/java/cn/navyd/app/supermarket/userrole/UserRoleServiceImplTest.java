package cn.navyd.app.supermarket.userrole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.BaseServiceTest;

public class UserRoleServiceImplTest extends BaseServiceTest {
  @Autowired
  private UserRoleService userRoleService;
  
  private UserRoleDO userRole;
  
  @BeforeEach
  void setup() {
    int id = 1;
    userRole = userRoleService.getByPrimaryKey(id).get();
  }
  
  @Test
  void getByPrimaryKeyTest() {
    int id = 1;
    assertThat(userRoleService.getByPrimaryKey(id))
      .isPresent()
      .get()
      .matches(ur -> ur.getId() == id);
  }
  
  @Transactional
  @Test
  void saveTest() {
    UserRoleDO saveUserRole = new UserRoleDO();
    final int userId = 1, roleId = 4;
    saveUserRole.setRoleId(roleId);
    saveUserRole.setUserId(userId);
    
    assertThat(userRoleService.save(saveUserRole))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(saveUserRole, BASE_PROPERTIES);
  }
  
  @Transactional
  @Test
  void saveDupliateTest() {
    UserRoleDO saveUserRole = new UserRoleDO();
    final int userId = userRole.getUserId(), roleId = userRole.getRoleId();
    saveUserRole.setRoleId(roleId);
    saveUserRole.setUserId(userId);
    
    assertThatThrownBy(() -> userRoleService.save(saveUserRole)).isInstanceOf(DuplicateUserRoleException.class);
  }
  
  @Transactional
  @Test
  void updateByPrimaryKeyTest() {
    final int userId = 1, roleId = 4;
    UserRoleDO updateUserRole = new UserRoleDO();
    updateUserRole.setId(userRole.getId());
    updateUserRole.setRoleId(roleId);
    updateUserRole.setUserId(userId);
    
    assertThat(userRoleService.save(updateUserRole))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(updateUserRole, BASE_PROPERTIES);
  }
  
  @Transactional
  @Test
  void updateByPrimaryKeyNotFoundExceptionTest() {
    final int userId = 1, roleId = 4;
    UserRoleDO updateUserRole = new UserRoleDO();
    updateUserRole.setId(Integer.MAX_VALUE);
    updateUserRole.setRoleId(roleId);
    updateUserRole.setUserId(userId);
    
    assertThatThrownBy(() -> userRoleService.updateByPrimaryKey(updateUserRole)).isInstanceOf(UserRoleNotFoundException.class);
  }
  
}
