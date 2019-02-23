package cn.navyd.app.supermarket.userrole;

import java.util.Collection;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import cn.navyd.app.supermarket.base.BaseDao;

@CacheNamespace(readWrite=false)
@Mapper
public interface UserRoleDao extends BaseDao<UserRoleDO> {
  static final String RESULT_MAP_ID = "userRoleMap";
  // ReadOnlyDao方法
  
  @SelectProvider(type=UserRoleSqlProvider.class, method="countTotalRows")
  int countTotalRows();
  
  @SelectProvider(type=UserRoleSqlProvider.class, method="countRowsByLastId") 
  int countRowsByLastId(Integer lastId);
  
  @SelectProvider(type=UserRoleSqlProvider.class, method="getByPrimaryKey")
  @Results(id=RESULT_MAP_ID, value={
      @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
      @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
      @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
      @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
      @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
  })
  UserRoleDO getByPrimaryKey(Integer id);
  
  @SelectProvider(type=UserRoleSqlProvider.class, method="listPage") 
  Collection<UserRoleDO> listPage(Integer pageNumber, Integer pageSize, Integer lastId);
 
  // baseDao方法
  
  @InsertProvider(type=UserRoleSqlProvider.class, method="save")
  void save(UserRoleDO bean);
  
  @UpdateProvider(type=UserRoleSqlProvider.class, method="updateByPrimaryKey")
  void updateByPrimaryKey(UserRoleDO bean);
  
  @DeleteProvider(type=UserRoleSqlProvider.class, method="removeByPrimaryKey")
  void removeByPrimaryKey(Integer id);
}
