package cn.navyd.app.supermarket.base.dao;

import java.util.Collection;
import org.apache.ibatis.jdbc.SQL;
import cn.navyd.app.supermarket.userrole.UserRoleDO;

public class UserRoleSqlProvider extends AbstractSqlProvider<UserRoleDO> {
  @Override
  protected String getTableName() {
    return "user_role";
  }

  @Override
  protected String getExtraColumns() {
    return "user_id, role_id";
  }

  public String save(UserRoleDO bean) {
    return new SQL() {
      {
        INSERT_INTO(getTableName()).VALUES("user_id", "#{userId}").VALUES("role_id", "#{roleId}");
      }
    }.toString();
  }

  public String updateByPrimaryKey(UserRoleDO bean) {
    return new SQL() {
      {
        UPDATE(getTableName());

        if (bean.getUserId() != null)
          SET("user_id = #{userId}");

        if (bean.getRoleId() != null)
          SET("role_id = #{roleId}");

        WHERE("id = #{id}");
      }
    }.toString();
  }

  // user role dao 方法

  /**
   * 该方法存在bug.虽然能够正确执行，但是无法主动修改集合userrole.id主键
   * @param collection
   * @return
   */
  public String saveAll(Collection<UserRoleDO> collection) {
    final StringBuilder sb = new StringBuilder();
    sb.append("INSERT INTO ")
      .append(getTableName())
      .append(" (user_id, role_id) ")
      .append("VALUES ");

    for (int i = 0; i < collection.size(); i++)
      sb.append(String.format("(#{collection[%d].userId}, #{collection[%d].roleId}), ", i, i));
    sb.delete(sb.length() - 2, sb.length());
    return sb.toString();
  }
}
