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
import cn.navyd.app.supermarket.base.BaseDao;
import cn.navyd.app.supermarket.base.dao.UserSqlProvider;

@CacheNamespace(readWrite=false)
@Mapper
public interface UserDao extends BaseDao<UserDO> {
  // ReadOnlyDao方法
  
  @SelectProvider(type=UserSqlProvider.class, method="countTotalRows")
  int countTotalRows();
  
  @SelectProvider(type=UserSqlProvider.class, method="countRowsByLastId") 
  int countRowsByLastId(Integer lastId);
  
  @SelectProvider(type=UserSqlProvider.class, method="getByPrimaryKey") 
  @Results(id=UserSqlProvider.RESULT_MAP_ID, value=@Result(column = "is_enabled", property = "enabled"))
  UserDO getByPrimaryKey(Integer id);
  
  @SelectProvider(type=UserSqlProvider.class, method="listPage") 
  @ResultMap(UserSqlProvider.RESULT_MAP_ID)
  Collection<UserDO> listPage(Integer pageNum, Integer pageSize, Integer lastId);
  
  // baseDao方法
  
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
  @ResultMap(UserSqlProvider.RESULT_MAP_ID)
  UserDO getByUsername(String username);

  /**
   * 通过email获取user
   * 
   * @param email
   * @return
   */
  @SelectProvider(type=UserSqlProvider.class, method="getByEmail") 
  @ResultMap(UserSqlProvider.RESULT_MAP_ID)
  UserDO getByEmail(String email);
}
