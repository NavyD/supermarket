package cn.navyd.app.supermarket.role;

import java.util.Collection;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import cn.navyd.app.supermarket.base.BaseDao;
import cn.navyd.app.supermarket.base.dao.RoleSqlProvider;

@CacheNamespace(readWrite=false)
@Mapper
public interface RoleDao extends BaseDao<RoleDO> {
  static final String RESULT_MAP_ID = "roleResultMap";
  
  @SelectProvider(type=RoleSqlProvider.class, method="countTotalRows")
  int countTotalRows();
  
  @SelectProvider(type=RoleSqlProvider.class, method="countRowsByLastId") 
  int countRowsByLastId(Integer lastId);
  
  @SelectProvider(type=RoleSqlProvider.class, method="getByPrimaryKey") 
  @Results(id=RESULT_MAP_ID, value={
      @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
      @Result(column="role_name", property="name", jdbcType=JdbcType.VARCHAR),
      @Result(column="is_enabled", property="enabled", jdbcType=JdbcType.TINYINT),
      @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
      @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
  })
  RoleDO getByPrimaryKey(Integer id);
  
  @SelectProvider(type=RoleSqlProvider.class, method="listPage") 
  @ResultMap(RESULT_MAP_ID)
  Collection<RoleDO> listPage(Integer pageNumber, Integer pageSize, Integer lastId);
  
  // baseDao方法
  
  @InsertProvider(type=RoleSqlProvider.class, method="save")
  void save(RoleDO bean);
  
  @UpdateProvider(type=RoleSqlProvider.class, method="updateByPrimaryKey")
  void updateByPrimaryKey(RoleDO bean);
  
  @DeleteProvider(type=RoleSqlProvider.class, method="removeByPrimaryKey")
  void removeByPrimaryKey(Integer id);
  
  /**
   * 使用name获取唯一的role
   * @param name
   * @return
   */
  @SelectProvider(type=RoleSqlProvider.class, method="getByName")
  @ResultMap(RESULT_MAP_ID)
  RoleDO getByName(String name);
  
  /**
   * 通过user id查询指定的用户关联的roles
   * @param userId
   * @return
   */
  @SelectProvider(type=RoleSqlProvider.class, method="listByUserId")
  @ResultMap(RESULT_MAP_ID)
  Collection<RoleDO> listByUserId(Integer userId);
}
