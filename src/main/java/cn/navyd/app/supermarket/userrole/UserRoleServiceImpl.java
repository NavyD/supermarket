package cn.navyd.app.supermarket.userrole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.navyd.app.supermarket.base.AbstractBaseService;
import cn.navyd.app.supermarket.base.NotFoundException;
import cn.navyd.app.supermarket.base.ReadOnlyDao;
import cn.navyd.app.supermarket.role.RoleDO;
import cn.navyd.app.supermarket.role.RoleNotFoundException;
import cn.navyd.app.supermarket.user.UserDO;
import cn.navyd.app.supermarket.user.UserNotFoundException;

@Service
public class UserRoleServiceImpl extends AbstractBaseService<UserRoleDO> implements UserRoleService {
  @Autowired
  private ReadOnlyDao<UserDO> userDao;
  @Autowired
  private ReadOnlyDao<RoleDO> roleDao;
  
  public UserRoleServiceImpl(UserRoleDao dao) {
    super(dao);
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
