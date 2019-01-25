package cn.navyd.app.supermarket.base.dao;

import org.apache.ibatis.jdbc.SQL;

/**
 * 实现了一些通用的sql代码。
 * 不能子类不允许覆盖BaseSqlProvider的实现方法，否则mybatis会抛出异常
 * @author navyd
 *
 * @param <T>
 */
public abstract class BaseSqlProvider<T> {
  
  public final String countTotalRows() {
    return new SQL() {
      {
        SELECT("count(*)")
        .FROM(getTableName());
      }
    }.toString();
  }
  
  public final String countRowsByLastId(Integer lastId) {
    return new SQL() {
      {
        SELECT("count(*)")
        .FROM(getTableName());
        if (lastId != null)
          WHERE("id > #{lastId}");
      }
    }.toString();
  }
  
  public final String getByPrimaryKey(Integer id) {
    return new SQL() {
      {
        SELECT(getBaseColumns())
        .FROM(getTableName())
        .WHERE("id = #{id,jdbcType=INTEGER}");
      }
    }.toString();
  }
  
  public final String listPage(Integer pageNum, Integer pageSize, Integer lastId) {
    final int fromIndex = pageNum * pageSize, size = pageSize;
    return new SQL() {
      {
        SELECT(getBaseColumns())
        .FROM(getTableName());
        if (lastId != null) 
          WHERE("id > #{lastId}");
        
      }
    }.toString() + " LIMIT " + fromIndex + ", " + size;
  }
  
  public final String removeByPrimaryKey(Integer id) {
    return new SQL() {
      {
        DELETE_FROM(getTableName())
        .WHERE("id = #{id}");
      }
    }.toString();
  }
  
  // 不能覆盖
//  public abstract String save(T bean);
//  
//  public abstract String updateByPrimaryKey(T bean);
  
  protected abstract String getTableName(); 
  
  protected abstract String getBaseColumns(); 
}
