package cn.navyd.app.supermarket.supplier;

import org.apache.ibatis.jdbc.SQL;
import cn.navyd.app.supermarket.base.AbstractSqlProvider;

public class SupplierSqlProvider extends AbstractSqlProvider<SupplierDO> {
  private static final String BASE_COLUMNS = "id, supplier_name, gmt_create, gmt_modified";

  @Override
  protected String getBaseColumns() {
    return BASE_COLUMNS;
  }

  @Override
  protected String getTableName() {
    return "supplier";
  }

  public String save(SupplierDO bean) {
    SQL sql = new SQL();
    sql.INSERT_INTO("supplier");

    if (bean.getName() != null) {
      sql.VALUES("supplier_name", "#{name,jdbcType=VARCHAR}");
    }

    return sql.toString();
  }

  public String updateByPrimaryKey(SupplierDO bean) {
    SQL sql = new SQL();
    sql.UPDATE("supplier");

    if (bean.getName() != null) {
      sql.SET("supplier_name = #{name,jdbcType=VARCHAR}");
    }

    sql.WHERE("id = #{id,jdbcType=INTEGER}");

    return sql.toString();
  }

  public String getByName(String name) {
    return new SQL() {
      {
        SELECT(getBaseColumns())
        .FROM(getTableName())
        .WHERE("supplier_name = #{name}");
      }
    }.toString();
  }

}
