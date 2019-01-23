package cn.navyd.app.supermarket.role;

import java.util.Collection;
import cn.navyd.app.supermarket.base.BaseDao;

public interface RoleDao extends BaseDao<RoleDO> {
  Collection<RoleDO> listByUserId(Integer userId);
}
