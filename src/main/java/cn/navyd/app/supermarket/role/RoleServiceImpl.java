package cn.navyd.app.supermarket.role;

import java.util.Collection;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;
import cn.navyd.app.supermarket.base.AbstractBaseService;
import cn.navyd.app.supermarket.base.NotFoundException;

public class RoleServiceImpl extends AbstractBaseService<RoleDao, RoleDO> implements RoleService {
  public RoleServiceImpl(RoleDao dao) {
    super(dao);
  }

  @Override
  public Optional<RoleDO> getByName(String name) {
    Preconditions.checkArgument(!StringUtils.isEmpty(name));
    return Optional.ofNullable(dao.getByName(name));
  }

  @Override
  public Collection<RoleDO> listByUserId(Integer userId) {
    return dao.listByUserId(checkPrimaryKeyId(userId));
  }


  @Override
  protected void checkAssociativeNotFound(RoleDO bean) throws NotFoundException {
    
  }

}
