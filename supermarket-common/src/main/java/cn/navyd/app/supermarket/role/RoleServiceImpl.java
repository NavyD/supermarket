package cn.navyd.app.supermarket.role;

import static com.google.common.base.Preconditions.checkArgument;
import java.util.Collection;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import cn.navyd.app.supermarket.base.AbstractBaseService;
import cn.navyd.app.supermarket.base.DuplicateException;
import cn.navyd.app.supermarket.base.NotFoundException;
import cn.navyd.app.supermarket.base.ReadOnlyDao;
import cn.navyd.app.supermarket.user.UserDO;
import cn.navyd.app.supermarket.user.UserNotFoundException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleServiceImpl extends AbstractBaseService<RoleDO> implements RoleService {
  private final RoleDao roleDao;
  private ReadOnlyDao<UserDO> userDao;
  
  public RoleServiceImpl(RoleDao roleDao) {
    super(roleDao);
    this.roleDao = roleDao;
  }

  @Override
  public Optional<RoleDO> getByName(String name) {
    checkArgument(!StringUtils.isEmpty(name) && !StringUtils.isBlank(name));
    return Optional.ofNullable(roleDao.getByName(name));
  }

  @Override
  public Collection<RoleDO> listByUserId(Integer userId) {
    checkArgument(userId != null && userId >= 0);
    // 检查userId是否存在
    checkUserNotFoundByUserId(userId);
    return roleDao.listByUserId(userId);
  }

  @Override
  protected void checkAssociativeNotFound(RoleDO bean) throws NotFoundException {
    
  }
  
  @Override
  protected DuplicateException createDuplicateException(String message) {
    return new DuplicateRoleException(message);
  }
  
  @Override
  protected NotFoundException createNotFoundException(String message) {
    return new RoleNotFoundException(message);
  }
  
  private void checkUserNotFoundByUserId(Integer userId) {
    var user = userDao.getByPrimaryKey(userId);
    if (user == null)
      throw new UserNotFoundException("id: " + userId);
  }

}
