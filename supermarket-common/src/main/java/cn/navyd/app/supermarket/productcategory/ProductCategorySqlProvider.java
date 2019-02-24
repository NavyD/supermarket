package cn.navyd.app.supermarket.productcategory;

import org.apache.ibatis.jdbc.SQL;
import cn.navyd.app.supermarket.base.AbstractSqlProvider;

public class ProductCategorySqlProvider extends AbstractSqlProvider<ProductCategoryDO> {
  private static final String BASE_COLUMNS = "id, category_name, parent_id, gmt_create, gmt_modified";
  
  @Override
  protected String getBaseColumns() {
    return BASE_COLUMNS;
  }

  @Override
  protected String getTableName() {
    return "product_category";
  }
  
  public String save(ProductCategoryDO bean) {
    SQL sql = new SQL();
    sql.INSERT_INTO("product_category");
    
    if (bean.getCategoryName() != null) {
        sql.VALUES("category_name", "#{categoryName,jdbcType=VARCHAR}");
    }
    
    if (bean.getParentId() != null) {
        sql.VALUES("parent_id", "#{parentId,jdbcType=INTEGER}");
    }
    
    return sql.toString();
  }
  
  public String updateByPrimaryKey(ProductCategoryDO bean) {
    SQL sql = new SQL();
    sql.UPDATE("product_category");
    
    if (bean.getCategoryName() != null) {
        sql.SET("category_name = #{categoryName,jdbcType=VARCHAR}");
    }
    
    if (bean.getParentId() != null) {
        sql.SET("parent_id = #{parentId,jdbcType=INTEGER}");
    }
    
    sql.WHERE("id = #{id,jdbcType=INTEGER}");
    
    return sql.toString();
  }
  
  public String listChildrenByPrimaryKey(Integer id) {
    return new SQL() {
      {
        SELECT(getBaseColumns())
        .FROM(getTableName())
        .WHERE("parent_id = #{id}");
      }
    }.toString();
  }
  
  public String listDescendantsByPrimaryKey(Integer id) {
    StringBuilder sb = new StringBuilder();
    sb.append("with recursive cte (")
      .append(getBaseColumns())
      .append(") as (select ")
      .append(getBaseColumns())
      .append(" from ")
      .append(getTableName())
      .append(" where parent_id = #{id}")
      .append(" union all ")
      .append(" select ")
      .append(renameColumns("p", getBaseColumns()))
      .append(" from ")
      .append(getTableName()).append(" p ")
      .append(" inner join cte on p.parent_id = cte.id) ")
      .append("select ").append(getBaseColumns())
      .append(" from cte");
    return sb.toString();
  }

}
