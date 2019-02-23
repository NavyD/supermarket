package cn.navyd.app.supermarket.base.dao;

import org.apache.ibatis.jdbc.SQL;
import cn.navyd.app.supermarket.user.UserDO;

public final class UserSqlProvider extends AbstractSqlProvider<UserDO> {
  private static final String[] COLUMNS = {"id, username, hash_password, email, is_enabled, phone_number, failed_count, ",
      "gmt_create, gmt_modified"};
  private static final String BASE_COLUMNS = getColumnsString(COLUMNS);
  
  public String save(UserDO bean) {
    SQL sql = new SQL();
    sql.INSERT_INTO("user_info");
    
    if (bean.getUsername() != null) {
        sql.VALUES("username", "#{username,jdbcType=VARCHAR}");
    }
    
    if (bean.getHashPassword() != null) {
        sql.VALUES("hash_password", "#{hashPassword,jdbcType=CHAR}");
    }
    
    if (bean.getEmail() != null) {
        sql.VALUES("email", "#{email,jdbcType=VARCHAR}");
    }
    
    if (bean.getEnabled() != null) {
        sql.VALUES("is_enabled", "#{enabled,jdbcType=TINYINT}");
    }
    
    if (bean.getPhoneNumber() != null) {
        sql.VALUES("phone_number", "#{phoneNumber,jdbcType=CHAR}");
    }
    
    if (bean.getFailedCount() != null) {
        sql.VALUES("failed_count", "#{failedCount,jdbcType=TINYINT}");
    }
    
    return sql.toString();
  }
  
  public String updateByPrimaryKey(UserDO bean) {
    SQL sql = new SQL();
    sql.UPDATE("user_info");
    
    if (bean.getUsername() != null) {
        sql.SET("username = #{username,jdbcType=VARCHAR}");
    }
    
    if (bean.getHashPassword() != null) {
        sql.SET("hash_password = #{hashPassword,jdbcType=CHAR}");
    }
    
    if (bean.getEmail() != null) {
        sql.SET("email = #{email,jdbcType=VARCHAR}");
    }
    
    if (bean.getEnabled() != null) {
        sql.SET("is_enabled = #{enabled,jdbcType=TINYINT}");
    }
    
    if (bean.getPhoneNumber() != null) {
        sql.SET("phone_number = #{phoneNumber,jdbcType=CHAR}");
    }
    
    if (bean.getFailedCount() != null) {
        sql.SET("failed_count = #{failedCount,jdbcType=TINYINT}");
    }
    
    sql.WHERE("id = #{id,jdbcType=INTEGER}");
    
    return sql.toString();
  }
  
  
  public String getByUsername(String username) {
    return new SQL() {
      {
        SELECT(getBaseColumns())
        .FROM(getTableName())
        .WHERE("username = #{username}");
      }
    }.toString();
  }
  
  public String getByEmail(String email) {
    return new SQL() {
      {
        SELECT(getBaseColumns())
        .FROM(getTableName())
        .WHERE("email = #{email}");
      }
    }.toString();
  }
  
  @Override
  protected String getTableName() {
    return "user_info";
  }

  @Override
  protected String getBaseColumns() {
    return BASE_COLUMNS;
  }

}
