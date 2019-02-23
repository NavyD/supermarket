package cn.navyd.app.supermarket.product;

import java.util.Collection;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import cn.navyd.app.supermarket.base.BaseDao;
import cn.navyd.app.supermarket.base.dao.ProductSqlProvider;

public interface ProductDao extends BaseDao<ProductDO> {
  static final String RESULT_MAP_ID = "productResultMap";

  @SelectProvider(type=ProductSqlProvider.class, method="countTotalRows")
  @Override
  int countTotalRows();

  @SelectProvider(type=ProductSqlProvider.class, method="countRowsByLastId")
  @Override
  int countRowsByLastId(Integer lastId);

  @Results(id=RESULT_MAP_ID, value= {
      @Result(column = "product_name", property = "enabled"),
      @Result(column="product_unit", property="productUnit")})
  @SelectProvider(type=ProductSqlProvider.class, method="getByPrimaryKey")
  @Override
  ProductDO getByPrimaryKey(Integer id);

  @ResultMap(RESULT_MAP_ID)
  @SelectProvider(type=ProductSqlProvider.class, method="listPage")
  @Override
  Collection<ProductDO> listPage(Integer pageNumber, Integer pageSize, Integer lastId);

  @Override
  void save(ProductDO bean);

  @Override
  void updateByPrimaryKey(ProductDO bean);

  @Override
  void removeByPrimaryKey(Integer id);

}
