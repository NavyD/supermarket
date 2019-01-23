package cn.navyd.app.supermarket.role;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import cn.navyd.app.supermarket.base.BaseService;

public interface RoleService extends BaseService<RoleDO> {
  Optional<RoleDO> getByName(String name);
  
  /**
   * 获取指定user id对应所有的角色信息，包括禁用的
   * @param userId
   * @return
   */
  Collection<RoleDO> listByUserId(Integer userId);
  
  /**
   * 获取user关联激活的roles
   * @param userId
   * @return
   */
  default Collection<RoleDO> listEnabledByUserId(Integer userId) {
    return listByUserId(userId).stream()
        .filter(role -> role.getEnabled())
        .collect(Collectors.toUnmodifiableList());
  }
}
