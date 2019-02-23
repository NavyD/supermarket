package cn.navyd.app.supermarket.base.dao;

import org.apache.ibatis.jdbc.SQL;
import cn.navyd.app.supermarket.product.ProductDO;
//import static java;

public class ProductSqlProvider extends AbstractSqlProvider<ProductDO> {
  private static final String[] COLUMNS = {"id, product_name, production_date, shelf_life, product_unit, specification, ",
      "specification_unit, product_category_id, product_category_name, supplier_id, ",
      "supplier_name, gmt_create, gmt_modified"};
  
  private static final String BASE_COLUMNS = getColumnsString(COLUMNS); 

      
  @Override
  protected String getTableName() {
    return "product";
  }

  @Override
  protected String getBaseColumns() {
    return BASE_COLUMNS;
  }

  public String save(ProductDO bean) {
    SQL sql = new SQL();
    sql.INSERT_INTO("product");
    
    if (bean.getName() != null) {
        sql.VALUES("product_name", "#{name,jdbcType=VARCHAR}");
    }
    
    if (bean.getProductionDate() != null) {
        sql.VALUES("production_date", "#{productionDate,jdbcType=DATE}");
    }
    
    if (bean.getShelfLife() != null) {
        sql.VALUES("shelf_life", "#{shelfLife,jdbcType=SMALLINT}");
    }
    
    if (bean.getProductUnit() != null) {
        sql.VALUES("product_unit", "#{productUnit,jdbcType=TINYINT}");
    }
    
    if (bean.getSpecification() != null) {
        sql.VALUES("specification", "#{specification,jdbcType=INTEGER}");
    }
    
    if (bean.getSpecificationUnit() != null) {
        sql.VALUES("specification_unit", "#{specificationUnit,jdbcType=TINYINT}");
    }
    
    if (bean.getProductCategoryId() != null) {
        sql.VALUES("product_category_id", "#{productCategoryId,jdbcType=INTEGER}");
    }
    
    if (bean.getProductCategoryName() != null) {
        sql.VALUES("product_category_name", "#{productCategoryName,jdbcType=VARCHAR}");
    }
    
    if (bean.getSupplierId() != null) {
        sql.VALUES("supplier_id", "#{supplierId,jdbcType=INTEGER}");
    }
    
    if (bean.getSupplierName() != null) {
        sql.VALUES("supplier_name", "#{supplierName,jdbcType=VARCHAR}");
    }
    
    return sql.toString();
  }
  
  public String updateByPrimaryKey(ProductDO bean) {
    SQL sql = new SQL();
    sql.UPDATE("product");
    
    if (bean.getName() != null) {
        sql.SET("product_name = #{name,jdbcType=VARCHAR}");
    }
    
    if (bean.getProductionDate() != null) {
        sql.SET("production_date = #{productionDate,jdbcType=DATE}");
    }
    
    if (bean.getShelfLife() != null) {
        sql.SET("shelf_life = #{shelfLife,jdbcType=SMALLINT}");
    }
    
    if (bean.getProductUnit() != null) {
        sql.SET("product_unit = #{productUnit,jdbcType=TINYINT}");
    }
    
    if (bean.getSpecification() != null) {
        sql.SET("specification = #{specification,jdbcType=INTEGER}");
    }
    
    if (bean.getSpecificationUnit() != null) {
        sql.SET("specification_unit = #{specificationUnit,jdbcType=TINYINT}");
    }
    
    if (bean.getProductCategoryId() != null) {
        sql.SET("product_category_id = #{productCategoryId,jdbcType=INTEGER}");
    }
    
    if (bean.getProductCategoryName() != null) {
        sql.SET("product_category_name = #{productCategoryName,jdbcType=VARCHAR}");
    }
    
    if (bean.getSupplierId() != null) {
        sql.SET("supplier_id = #{supplierId,jdbcType=INTEGER}");
    }
    
    if (bean.getSupplierName() != null) {
        sql.SET("supplier_name = #{supplierName,jdbcType=VARCHAR}");
    }
    
    sql.WHERE("id = #{id,jdbcType=INTEGER}");
    
    return sql.toString();
  }

}
