package cn.navyd.app.supermarket.user;

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
import cn.navyd.app.supermarket.base.dao.UserSqlProvider;

@CacheNamespace(readWrite=false)
@Mapper
public interface UserDao extends BaseDao<UserDO> {
  static final String RESULT_MAP_ID = "userResultMap";
  // ReadOnlyDao方法
  
  @SelectProvider(type=UserSqlProvider.class, method="countTotalRows")
  int countTotalRows();
  
  @SelectProvider(type=UserSqlProvider.class, method="countRowsByLastId") 
  int countRowsByLastId(Integer lastId);
  
  @SelectProvider(type=UserSqlProvider.class, method="getByPrimaryKey") 
  @Results(id=RESULT_MAP_ID, value={
      @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
      @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
      @Result(column="hash_password", property="hashPassword", jdbcType=JdbcType.CHAR),
      @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
      @Result(column="is_enabled", property="enabled", jdbcType=JdbcType.TINYINT),
      @Result(column="phone_number", property="phoneNumber", jdbcType=JdbcType.CHAR),
      @Result(column="failed_count", property="failedCount", jdbcType=JdbcType.TINYINT),
      @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
      @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
  })
  UserDO getByPrimaryKey(Integer id);
  
  @SelectProvider(type=UserSqlProvider.class, method="listPage") 
  @ResultMap(RESULT_MAP_ID)
  Collection<UserDO> listPage(Integer pageNumber, Integer pageSize, Integer lastId);
  
  @InsertProvider(type=UserSqlProvider.class, method="save")
  void save(UserDO bean);
  
  @UpdateProvider(type=UserSqlProvider.class, method="updateByPrimaryKey")
  void updateByPrimaryKey(UserDO bean);
  
  @DeleteProvider(type=UserSqlProvider.class, method="removeByPrimaryKey")
  void removeByPrimaryKey(Integer id);
  
  // userdao 方法
  
  /**
   * 通过username获取指定的UserDO
   * 
   * @return
   */
  @SelectProvider(type=UserSqlProvider.class, method="getByUsername") 
  @ResultMap(RESULT_MAP_ID)
  UserDO getByUsername(String username);

  /**
   * 通过email获取user
   * 
   * @param email
   * @return
   */
  @SelectProvider(type=UserSqlProvider.class, method="getByEmail") 
  @ResultMap(RESULT_MAP_ID)
  UserDO getByEmail(String email);
}
