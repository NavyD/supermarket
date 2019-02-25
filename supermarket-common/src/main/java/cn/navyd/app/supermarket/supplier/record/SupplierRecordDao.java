package cn.navyd.app.supermarket.supplier.record;

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
public interface SupplierRecordDao extends BaseDao<SupplierRecordDO> {
  static final String ID = "supplierRecordResultMap";
  
  @SelectProvider(type=SupplierRecordSqlProvider.class, method="countTotalRows")
  @Override
  int countTotalRows();

  @SelectProvider(type=SupplierRecordSqlProvider.class, method="countRowsByLastId")
  @Override
  int countRowsByLastId(Integer lastId);

  @Results(id=ID, value={
      @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
      @Result(column="supplied_time", property="suppliedTime", jdbcType=JdbcType.TIMESTAMP),
      @Result(column="unit_price_supply", property="unitPriceSupply", jdbcType=JdbcType.DECIMAL),
      @Result(column="unit_price_return", property="unitPriceReturn", jdbcType=JdbcType.DECIMAL),
      @Result(column="product_id", property="productId", jdbcType=JdbcType.INTEGER),
      @Result(column="product_name", property="productName", jdbcType=JdbcType.VARCHAR),
      @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
      @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
  })
  @SelectProvider(type=SupplierRecordSqlProvider.class, method="getByPrimaryKey")
  @Override
  SupplierRecordDO getByPrimaryKey(Integer id);

  @ResultMap(ID)
  @SelectProvider(type=SupplierRecordSqlProvider.class, method="listPage")
  @Override
  Collection<SupplierRecordDO> listPage(Integer pageNumber, Integer pageSize,
      Integer lastId);

  @InsertProvider(type=SupplierRecordSqlProvider.class, method="save")
  @Override
  void save(SupplierRecordDO bean);

  @UpdateProvider(type=SupplierRecordSqlProvider.class, method="updateByPrimaryKey")
  @Override
  void updateByPrimaryKey(SupplierRecordDO bean);

  @DeleteProvider(type=SupplierRecordSqlProvider.class, method="removeByPrimaryKey")
  @Override
  void removeByPrimaryKey(Integer id);
  
  /**
   * 查询productId最新的供应记录
   * @param productId
   * @return
   */
  @ResultMap(ID)
  @SelectProvider(type=SupplierRecordSqlProvider.class, method="getLastByProductId")
  SupplierRecordDO getLastByProductId(Integer productId);
}
