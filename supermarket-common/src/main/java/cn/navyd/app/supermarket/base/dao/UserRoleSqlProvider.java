package cn.navyd.app.supermarket.base.dao;

import org.apache.ibatis.jdbc.SQL;
import cn.navyd.app.supermarket.userrole.UserRoleDO;

public class UserRoleSqlProvider extends AbstractSqlProvider<UserRoleDO> {
  private static final String BASE_COLUMNS = "id, user_id, role_id, gmt_create, gmt_modified";
  
  @Override
  protected String getTableName() {
    return "user_role";
  }

  public String save(UserRoleDO bean) {
    SQL sql = new SQL();
    sql.INSERT_INTO("user_role");
    
    if (bean.getUserId() != null) {
        sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
    }
    
    if (bean.getRoleId() != null) {
        sql.VALUES("role_id", "#{roleId,jdbcType=INTEGER}");
    }
    
    return sql.toString();
  }

  public String updateByPrimaryKey(UserRoleDO bean) {
    SQL sql = new SQL();
    sql.UPDATE("user_role");
    
    if (bean.getUserId() != null) {
        sql.SET("user_id = #{userId,jdbcType=INTEGER}");
    }
    
    if (bean.getRoleId() != null) {
        sql.SET("role_id = #{roleId,jdbcType=INTEGER}");
    }
    
    sql.WHERE("id = #{id,jdbcType=INTEGER}");
    
    return sql.toString();
  }

  @Override
  protected String getBaseColumns() {
    return BASE_COLUMNS;
  }
}
