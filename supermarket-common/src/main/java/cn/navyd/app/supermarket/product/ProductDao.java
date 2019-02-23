package cn.navyd.app.supermarket.product;

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

@Mapper
@CacheNamespace(readWrite=false)
public interface ProductDao extends BaseDao<ProductDO> {
  static final String RESULT_MAP_ID = "productResultMap";

  @SelectProvider(type=ProductSqlProvider.class, method="countTotalRows")
  @Override
  int countTotalRows();

  @SelectProvider(type=ProductSqlProvider.class, method="countRowsByLastId")
  @Override
  int countRowsByLastId(Integer lastId);

  @Results(id=RESULT_MAP_ID, value={
    @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
    @Result(column="product_name", property="name", jdbcType=JdbcType.VARCHAR),
    @Result(column="production_date", property="productionDate", jdbcType=JdbcType.DATE),
    @Result(column="shelf_life", property="shelfLife", jdbcType=JdbcType.SMALLINT),
    @Result(column="product_unit", property="productUnit", jdbcType=JdbcType.TINYINT),
    @Result(column="specification", property="specification", jdbcType=JdbcType.INTEGER),
    @Result(column="specification_unit", property="specificationUnit", jdbcType=JdbcType.TINYINT),
    @Result(column="product_category_id", property="productCategoryId", jdbcType=JdbcType.INTEGER),
    @Result(column="product_category_name", property="productCategoryName", jdbcType=JdbcType.VARCHAR),
    @Result(column="supplier_id", property="supplierId", jdbcType=JdbcType.INTEGER),
    @Result(column="supplier_name", property="supplierName", jdbcType=JdbcType.VARCHAR),
    @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
    @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
})
  @SelectProvider(type=ProductSqlProvider.class, method="getByPrimaryKey")
  @Override
  ProductDO getByPrimaryKey(Integer id);

  @ResultMap(RESULT_MAP_ID)
  @SelectProvider(type=ProductSqlProvider.class, method="listPage")
  @Override
  Collection<ProductDO> listPage(Integer pageNumber, Integer pageSize, Integer lastId);

  @InsertProvider(type=ProductSqlProvider.class, method="save")
  @Override
  void save(ProductDO bean);

  @UpdateProvider(type=ProductSqlProvider.class, method="updateByPrimaryKey")
  @Override
  void updateByPrimaryKey(ProductDO bean);

  @DeleteProvider(type=ProductSqlProvider.class, method="removeByPrimaryKey")
  @Override
  void removeByPrimaryKey(Integer id);
}
