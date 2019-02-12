package cn.navyd.app.supermarket.base.dao;

import org.apache.ibatis.jdbc.SQL;
import cn.navyd.app.supermarket.user.UserDO;

public final class UserSqlProvider extends AbstractSqlProvider<UserDO> {
  public static final String RESULT_MAP_ID = "userResultMap";

  private static final String EXTRA_COLUMNS =
      "username, hash_password, icon_path, email, is_enabled, phone_number, failed_count";
  private static final String TABLE_NAME = "user_info";

  public String save(UserDO bean) {
    return new SQL() {
      {
        INSERT_INTO(getTableName())
        .VALUES("username", "#{username}")
        .VALUES("hash_password", "#{hashPassword}")
        .VALUES("email", "#{email}")
        .VALUES("icon_path", "#{iconPath,jdbcType=VARCHAR}")
        .VALUES("phone_number", "#{phoneNumber,jdbcType=CHAR}");
        
        if (bean.getEnabled() != null)
          VALUES("is_enabled", "#{enabled}");
        
        if (bean.getFailedCount() != null)
          VALUES("failed_count", "#{failedCount}");
      }
    }.toString();
  }
  
  public String updateByPrimaryKey(UserDO bean) {
    return new SQL() {
      {
        UPDATE(getTableName());
        
        if (bean.getUsername() != null)
          SET("username = #{username}");
        
        if (bean.getHashPassword() != null)
          SET("hash_password = #{hashPassword}");
        
        if (bean.getIconPath() != null)
          SET("icon_path = #{iconPath}");
        
        if (bean.getEmail() != null)
          SET("email = #{email}");
        
        if (bean.getEnabled() != null)
          SET("is_enabled = #{enabled}");
        
        if (bean.getPhoneNumber() != null)
          SET("phone_number = #{phoneNumber}");
        
        if (bean.getFailedCount() != null)
          SET("failed_count = #{failedCount}");
        
        WHERE("id = #{id}");
      }
    }.toString();
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
    return TABLE_NAME;
  }

  @Override
  protected String getExtraColumns() {
    return EXTRA_COLUMNS;
  }

}
