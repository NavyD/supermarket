package cn.navyd.app.supermarket.supplier;

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

@CacheNamespace(readWrite=false)
@Mapper
public interface SupplierDao extends BaseDao<SupplierDO> {
  static final String ID = "supplierResultMap";

  @SelectProvider(type=SupplierSqlProvider.class, method="countTotalRows")
  @Override
  int countTotalRows();

  @SelectProvider(type=SupplierSqlProvider.class, method="countRowsByLastId")
  @Override
  int countRowsByLastId(Integer lastId);

  @SelectProvider(type=SupplierSqlProvider.class, method="getByPrimaryKey")
  @Results(id=ID, value={
      @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
      @Result(column="supplier_name", property="name", jdbcType=JdbcType.VARCHAR),
      @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
      @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
  })
  @Override
  SupplierDO getByPrimaryKey(Integer id);

  @ResultMap(ID)
  @SelectProvider(type=SupplierSqlProvider.class, method="listPage")
  @Override
  Collection<SupplierDO> listPage(Integer pageNumber, Integer pageSize, Integer lastId);

  @InsertProvider(type=SupplierSqlProvider.class, method="save")
  @Override
  void save(SupplierDO bean);

  @UpdateProvider(type=SupplierSqlProvider.class, method="updateByPrimaryKey")
  @Override
  void updateByPrimaryKey(SupplierDO bean);

  @DeleteProvider(type=SupplierSqlProvider.class, method="removeByPrimaryKey")
  @Override
  void removeByPrimaryKey(Integer id);

  @ResultMap(ID)
  @SelectProvider(type=SupplierSqlProvider.class, method="getByName")
  SupplierDO getByName(String name);
  
}
