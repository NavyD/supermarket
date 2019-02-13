package cn.navyd.app.supermarket.userrole;

import cn.navyd.app.supermarket.base.AbstractBaseService;
import cn.navyd.app.supermarket.base.NotFoundException;
import cn.navyd.app.supermarket.base.ReadOnlyDao;
import cn.navyd.app.supermarket.role.RoleDO;
import cn.navyd.app.supermarket.role.RoleNotFoundException;
import cn.navyd.app.supermarket.user.UserDO;
import cn.navyd.app.supermarket.user.UserNotFoundException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleServiceImpl extends AbstractBaseService<UserRoleDO> implements UserRoleService {
  private ReadOnlyDao<UserDO> userDao;
  private ReadOnlyDao<RoleDO> roleDao;
  
  public UserRoleServiceImpl(UserRoleDao userRoleDao) {
    super(userRoleDao);
  }

  @Override
  protected void checkAssociativeNotFound(UserRoleDO bean) throws NotFoundException {
    final Integer userId = bean.getUserId(), roleId = bean.getRoleId();
    if (userId != null && userDao.getByPrimaryKey(userId) == null)
      throw new UserNotFoundException("id: " + userId);
      
    if (roleId != null && roleDao.getByPrimaryKey(roleId) == null)
      throw new RoleNotFoundException("id: " + roleId);
  }

}
