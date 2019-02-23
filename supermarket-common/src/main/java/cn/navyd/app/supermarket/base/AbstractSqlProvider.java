package cn.navyd.app.supermarket.base;

import org.apache.ibatis.jdbc.SQL;

/**
 * 实现了一些通用的sql代码。 不能子类不允许覆盖BaseSqlProvider的实现方法，否则mybatis会抛出异常
 * 该类已提供的通用{@link cn.navyd.app.supermarket.base.BaseDao} sql实现为：
 * <ol>
 * <li>{@linkplain cn.navyd.app.supermarket.base.BaseDao#countTotalRows() countTotalRows()}
 * <li>{@linkplain cn.navyd.app.supermarket.base.BaseDao#countRowsByLastId() countRowsByLastId()}
 * <li>{@linkplain cn.navyd.app.supermarket.base.BaseDao#getByPrimaryKey() getByPrimaryKey()}
 * <li>{@linkplain cn.navyd.app.supermarket.base.BaseDao#listPage() listPage()}
 * <li>{@linkplain cn.navyd.app.supermarket.base.BaseDao#removeByPrimaryKey() removeByPrimaryKey()}
 * </ol>
 * 对于以上的方法仅需要提供表名，列名即可
 * <p>
 * 对于{@linkplain cn.navyd.app.supermarket.base.BaseDao#save() save()}
 * {@linkplain cn.navyd.app.supermarket.base.BaseDao#updateByPrimaryKey() updateByPrimaryKey()}
 * 需要子类主动实现。
 * 
 * @author navyd
 *
 * @param <T>
 */
public abstract class AbstractSqlProvider<T extends BaseDO> {
  @Deprecated
  protected static final String BASE_COLUMNS = "id, gmt_create, gmt_modified";
  protected static final String COLUMN_DELITER = ",";
  
  /**
   * 将指定的colums string数组连接','为一个字符串。仅连接各个字符串，不做其他
   * @param columns
   * @return
   */
  protected static String getColumnsString(String[] columns) {
    StringBuilder sb = new StringBuilder();
    for (String cols : columns) {
        sb.append(cols);
    }
    return sb.toString();
  }

  protected static String renameColumns(String table, String columns) {
    StringBuilder sb = new StringBuilder();
    String delimiter = COLUMN_DELITER;
    String[] names = columns.split(delimiter);
    for (String col : names) {
      sb.append(table).append('.').append(col.trim()).append(", ");
    }
    sb.delete(sb.length() - 2, sb.length());
    return sb.toString();
  }

  public final String countTotalRows() {
    return new SQL() {
      {
        SELECT("count(*)").FROM(getTableName());
      }
    }.toString();
  }

  public final String countRowsByLastId(Integer lastId) {
    return new SQL() {
      {
        SELECT("count(*)").FROM(getTableName());
        if (lastId != null)
          WHERE("id > #{lastId}");
      }
    }.toString();
  }

  public final String getByPrimaryKey(Integer id) {
    return new SQL() {
      {
        SELECT(getBaseColumns()).FROM(getTableName()).WHERE("id = #{id,jdbcType=INTEGER}");
      }
    }.toString();
  }

  public final String listPage(Integer pageNumber, Integer pageSize, Integer lastId) {
    final int fromIndex = pageNumber * pageSize, size = pageSize;
    return new SQL() {
      {
        SELECT(getBaseColumns()).FROM(getTableName());
        if (lastId != null)
          WHERE("id > #{lastId}");

      }
    }.toString() + " LIMIT " + fromIndex + ", " + size;
  }

  public final String removeByPrimaryKey(Integer id) {
    return new SQL() {
      {
        DELETE_FROM(getTableName()).WHERE("id = #{id}");
      }
    }.toString();
  }
  
  // 不能覆盖
  // public abstract String save(T bean);
  //
  // public abstract String updateByPrimaryKey(T bean);

  /**
   * 获取数据表的列名用','分隔的字符串。该方法返回的列将与DO对象属性相对应
   * 
   * @return
   */
  protected abstract String getBaseColumns();

  protected abstract String getTableName();

  /**
   * 获取除{@link #BASE_COLUMNS}外的列名。
   * 
   * @see #getBaseColumns()
   * @return
   */
  @Deprecated
  protected String getExtraColumns() {
    return "";
  }
}
