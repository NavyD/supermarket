package cn.navyd.app.supermarket.productcategory;

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
public interface ProductCategoryDao extends BaseDao<ProductCategoryDO> {
  static final String RESULT_MAP_ID = "productCategoryresultMap";
  
  @SelectProvider(type=ProductCategorySqlProvider.class, method="countTotalRows")
  @Override
  int countTotalRows();

  @SelectProvider(type=ProductCategorySqlProvider.class, method="countRowsByLastId")
  @Override
  int countRowsByLastId(Integer lastId);
  
  @SelectProvider(type=ProductCategorySqlProvider.class, method="getByPrimaryKey")
  @Results(id=RESULT_MAP_ID, value={
      @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
      @Result(column="category_name", property="categoryName", jdbcType=JdbcType.VARCHAR),
      @Result(column="parent_id", property="parentId", jdbcType=JdbcType.INTEGER),
      @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
      @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
  })
  @Override
  ProductCategoryDO getByPrimaryKey(Integer id);

  @SelectProvider(type=ProductCategorySqlProvider.class, method="listPage")
  @ResultMap(RESULT_MAP_ID)
  @Override
  Collection<ProductCategoryDO> listPage(Integer pageNumber, Integer pageSize, Integer lastId);

  @InsertProvider(type=ProductCategorySqlProvider.class, method="save")
  @Override
  void save(ProductCategoryDO bean);

  @UpdateProvider(type=ProductCategorySqlProvider.class, method="updateByPrimaryKey")
  @Override
  void updateByPrimaryKey(ProductCategoryDO bean);

  @DeleteProvider(type=ProductCategorySqlProvider.class, method="removeByPrimaryKey")
  @Override
  void removeByPrimaryKey(Integer id);

  /**
   *  获取当前节点下的孩子节点
   * @param id
   * @return
   */
  @ResultMap(RESULT_MAP_ID)
  @SelectProvider(type=ProductCategorySqlProvider.class, method="listChildrenByPrimaryKey")
  Collection<ProductCategoryDO> listChildrenByPrimaryKey(Integer id);

  /**
   * 获取当前节点下所有孩子节点
   * @param id
   * @return
   */
  @ResultMap(RESULT_MAP_ID)
  @SelectProvider(type=ProductCategorySqlProvider.class, method="listDescendantsByPrimaryKey")
  Collection<ProductCategoryDO> listDescendantsByPrimaryKey(Integer id);
}
