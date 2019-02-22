package cn.navyd.app.supermarket.userrole;

import java.util.Collection;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import cn.navyd.app.supermarket.base.BaseDao;
import cn.navyd.app.supermarket.base.dao.UserRoleSqlProvider;

@CacheNamespace(readWrite=false)
@Mapper
public interface UserRoleDao extends BaseDao<UserRoleDO> {
  // ReadOnlyDao方法
  
  @SelectProvider(type=UserRoleSqlProvider.class, method="countTotalRows")
  int countTotalRows();
  
  @SelectProvider(type=UserRoleSqlProvider.class, method="countRowsByLastId") 
  int countRowsByLastId(Integer lastId);
  
  @SelectProvider(type=UserRoleSqlProvider.class, method="getByPrimaryKey") 
  UserRoleDO getByPrimaryKey(Integer id);
  
  @SelectProvider(type=UserRoleSqlProvider.class, method="listPage") 
  Collection<UserRoleDO> listPage(Integer pageNum, Integer pageSize, Integer lastId);
 
  // baseDao方法
  
  @InsertProvider(type=UserRoleSqlProvider.class, method="save")
  void save(UserRoleDO bean);
  
  @UpdateProvider(type=UserRoleSqlProvider.class, method="updateByPrimaryKey")
  void updateByPrimaryKey(UserRoleDO bean);
  
  @DeleteProvider(type=UserRoleSqlProvider.class, method="removeByPrimaryKey")
  void removeByPrimaryKey(Integer id);
}
