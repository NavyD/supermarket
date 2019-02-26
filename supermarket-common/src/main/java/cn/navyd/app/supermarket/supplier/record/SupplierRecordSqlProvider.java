package cn.navyd.app.supermarket.supplier.record;

import org.apache.ibatis.jdbc.SQL;
import cn.navyd.app.supermarket.base.AbstractSqlProvider;

public class SupplierRecordSqlProvider extends AbstractSqlProvider<SupplierRecordDO> {
  private static final String BASE_COLUMNS =
      "id, supplied_time, unit_price_supply, unit_price_return, product_id, gmt_create, gmt_modified";

  @Override
  protected String getBaseColumns() {
    return BASE_COLUMNS;
  }

  @Override
  protected String getTableName() {
    return "supplier_record";
  }

  public String save(SupplierRecordDO bean) {
    SQL sql = new SQL();
    sql.INSERT_INTO("supplier_record");

    if (bean.getSuppliedTime() != null) {
      sql.VALUES("supplied_time", "#{suppliedTime,jdbcType=TIMESTAMP}");
    }

    if (bean.getUnitPriceSupply() != null) {
      sql.VALUES("unit_price_supply", "#{unitPriceSupply,jdbcType=DECIMAL}");
    }

    if (bean.getUnitPriceReturn() != null) {
      sql.VALUES("unit_price_return", "#{unitPriceReturn,jdbcType=DECIMAL}");
    }

    if (bean.getProductId() != null) {
      sql.VALUES("product_id", "#{productId,jdbcType=INTEGER}");
    }

    return sql.toString();
  }

  public String updateByPrimaryKey(SupplierRecordDO bean) {
    SQL sql = new SQL();
    sql.UPDATE("supplier_record");

    if (bean.getSuppliedTime() != null) {
      sql.SET("supplied_time = #{suppliedTime,jdbcType=TIMESTAMP}");
    }

    if (bean.getUnitPriceSupply() != null) {
      sql.SET("unit_price_supply = #{unitPriceSupply,jdbcType=DECIMAL}");
    }

    if (bean.getUnitPriceReturn() != null) {
      sql.SET("unit_price_return = #{unitPriceReturn,jdbcType=DECIMAL}");
    }

    if (bean.getProductId() != null) {
      sql.SET("product_id = #{productId,jdbcType=INTEGER}");
    }

    sql.WHERE("id = #{id,jdbcType=INTEGER}");

    return sql.toString();
  }
  
  public String getLastByProductId(Integer productId) {
    return new SQL() {
      {
        SELECT(renameColumns("a", getBaseColumns()))
        .FROM(getTableName() + " a ")
        .LEFT_OUTER_JOIN(getTableName() + " b on a.product_id = b.product_id and a.supplied_time < b.supplied_time")
        .WHERE("b.product_id is null and a.product_id = #{productId}");
      }
    }.toString();
  }
}
