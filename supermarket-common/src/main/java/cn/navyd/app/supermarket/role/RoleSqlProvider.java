package cn.navyd.app.supermarket.role;

import org.apache.ibatis.jdbc.SQL;
import cn.navyd.app.supermarket.base.AbstractSqlProvider;

public class RoleSqlProvider extends AbstractSqlProvider<RoleDO> {
  private static final String BASE_COLUMNS = "id, role_name, is_enabled, gmt_create, gmt_modified";
  
  @Override
  protected String getTableName() {
    return "role_info";
  }
  
  public String save(RoleDO bean) {
    SQL sql = new SQL();
    sql.INSERT_INTO(getTableName());
    
    if (bean.getName() != null) {
        sql.VALUES("role_name", "#{name,jdbcType=VARCHAR}");
    }
    
    if (bean.getEnabled() != null) {
        sql.VALUES("is_enabled", "#{enabled,jdbcType=TINYINT}");
    }
    
    return sql.toString();
  }
  
  public String updateByPrimaryKey(RoleDO bean) {
    SQL sql = new SQL();
    sql.UPDATE(getTableName());
    
    if (bean.getName() != null) {
        sql.SET("role_name = #{name,jdbcType=VARCHAR}");
    }
    
    if (bean.getEnabled() != null) {
        sql.SET("is_enabled = #{enabled,jdbcType=TINYINT}");
    }
    
    sql.WHERE("id = #{id,jdbcType=INTEGER}");
    
    return sql.toString();
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

  @Override
  protected String getBaseColumns() {
    return BASE_COLUMNS;
  }
}
