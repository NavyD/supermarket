package cn.navyd.app.supermarket.base.dao;

import org.apache.ibatis.jdbc.SQL;
import cn.navyd.app.supermarket.role.RoleDO;

public class RoleSqlProvider extends AbstractSqlProvider<RoleDO> {
  private static final String EXTRA_COLUMNS = "role_name, is_enabled";
  
  @Override
  protected String getTableName() {
    return "role_info";
  }

  @Override
  protected String getExtraColumns() {
    return EXTRA_COLUMNS;
  }
  
  public String save(RoleDO bean) {
    return new SQL() {
      {
        INSERT_INTO(getTableName())
        .VALUES("role_name", "#{name}");
        
        if (bean.getEnabled() != null)
          VALUES("is_enabled", "#{enabled}");
      }
    }.toString();
  }
  
  public String updateByPrimaryKey(RoleDO bean) {
    return new SQL() {
      {
        UPDATE(getTableName());
        
        if (bean.getName() != null)
          SET("role_name = #{name}");
        
        if (bean.getEnabled() != null)
          SET("is_enabled = #{enabled}");
        WHERE("id = #{id}");
      }
    }.toString();
  }
  
  // roleDao方法
  
  public String getByName(String name) {
    return new SQL() {
      {
        SELECT(getBaseColumns())
        .FROM(getTableName())
        .WHERE("role_name = #{name}");
      }
    }.toString();
  }
  
  public String listByUserId(Integer userId) {
    return new SQL() {
      {
        SELECT(renameColumns("role", getBaseColumns()))
        .FROM(getTableName() + " role")
        .INNER_JOIN("user_role ur ON role.id = ur.role_id")
        .WHERE("ur.user_id = #{userId}");
      }
    }.toString();
  }
  
  // cte 表达式查询
//  public String listByPrimaryKey(Integer id) {
//    String sql = "WITH RECURSIVE cte (" + getBaseColumns() + ") AS (\n";
//    sql += new SQL() {
//      {
//        SELECT(getBaseColumns())
//        .FROM(getTableName())
//        .WHERE("id = #{id}");
//      }
//    }.toString();
//    sql += "\nUNION ALL\n";
//    sql += new SQL() {
//      {
//        String tableName = "p";
//        SELECT(renameColumns(tableName, getBaseColumns()))
//        .FROM(getTableName() + " " + tableName)
//        .INNER_JOIN("cte ON p.parent_id = cte.id");
//      }
//    }.toString();
//    sql += "\n)\n";
//    sql += new SQL() {
//      {
//        SELECT(getBaseColumns())
//        .FROM("cte");
//      }
//    }.toString();
//    return sql;
//  }
}
