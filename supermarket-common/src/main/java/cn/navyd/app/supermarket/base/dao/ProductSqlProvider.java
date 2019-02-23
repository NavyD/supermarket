package cn.navyd.app.supermarket.base.dao;

import java.sql.JDBCType;
import org.apache.ibatis.jdbc.SQL;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;
import cn.navyd.app.supermarket.product.ProductDO;
//import static java;

public class ProductSqlProvider extends AbstractSqlProvider<ProductDO> {
  static class ProductSqlTable extends BasicSqlTable {
    final SqlColumn<String> productName = column("product_name", JDBCType.VARCHAR);
    final SqlColumn<String> productionDate = column("production_date", JDBCType.VARCHAR);
    final SqlColumn<String> shelf_life = column("shelf_life", JDBCType.VARCHAR);
    final SqlColumn<String> product_unit = column("product_unit", JDBCType.TINYINT);
    final SqlColumn<String> specification = column("specification", JDBCType.VARCHAR);
    final SqlColumn<String> specification_unit = column("specification_unit", JDBCType.VARCHAR);
    final SqlColumn<String> product_category_id = column("product_category_id");
    final SqlColumn<String> product_category_name = column("product_category_name");
    final SqlColumn<String> supplier_id = column("supplier_id");
    final SqlColumn<String> supplier_name = column("product_category_id");
    
    protected ProductSqlTable(String name) {
      super(name);
    }
  }

  @Override
  protected String getTableName() {
    return "product";
  }

  @Override
  protected String getExtraColumns() {
    return "product_name, production_date, shelf_life, product_unit, specification, specification_unit, product_category_id, product_category_name, supplier_id, supplier_name";
  }

  public String save(ProductDO bean) {
    return new SQL() {
      {
        INSERT_INTO(getTableName())
        .VALUES("product_name", "#{name}")
        .VALUES("production_date", "#{productionDate}")
        .VALUES("product_unit", "#{productUnit}")
        .VALUES("specification", "#{specification}")
        .VALUES("specification_unit", "#{specificationUnit}")
        .VALUES("product_category_id", "#{productCategoryId}")
        .VALUES("product_category_name", "#{productCategoryName}")
        .VALUES("supplier_id", "#{supplierId}")
        .VALUES("supplier_name", "#{supplierName}");
        
        if (bean.getShelfLife() != null)
          VALUES("shelf_life", "#{shelfLife}");
      }
    }.toString();
  }

  public String updateByPrimaryKey(ProductDO bean) {
    return new SQL() {
      {
        UPDATE(getTableName());
        
        if (bean.getName() != null)
          SET("product_name = #{name}");
        
        if (bean.getProductionDate() != null)
          SET("production_date = #{productionDate}");
        
        if (bean.getShelfLife() != null)
          SET("shelf_life = #{shelfLife}");
        
        if (bean.getProductUnit() != null)
          SET("product_unit = #{productUnit}");
        SqlTable a;
        if (isNotNull(bean.getSpecification()))
          SET("specification = #{specification}");
        
        if (isNotNull(bean.getProductSpecificationUnit()))
          SET("specification = #{specification}");
        if (isNotNull(bean.getSpecification()))
          SET("specification = #{specification}");
        if (isNotNull(bean.getSpecification()))
          SET("specification = #{specification}");
        if (isNotNull(bean.getSpecification()))
          SET("specification = #{specification}");
      }
    }.toString();
  }

}
