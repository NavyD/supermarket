package cn.navyd.app.supermarket.base;

import java.util.Collection;
import org.apache.ibatis.annotations.Param;

public interface ReadOnlyDao<T extends PrimaryKey> {
  /**
   * 获取存在总数量
   * @return
   */
  int countTotalRows();
  
  /**
   * 返回大于lastId的数量。用于{@link #listPage(Integer, Integer, Integer)}lastId != null时计算剩余的分页数量
   * @param lastId
   * @return
   */
  int countRowsByLastId(@Param("lastId") Integer lastId);
  
  /**
   * 通过主键id获取指定对象信息。如果不存在则返回null。
   * <p>该方法返回的对象不允许修改，否则将导致缓存异常。
   * @param id
   * @return
   */
  T getByPrimaryKey(Integer id);
  
  /**
   * 分页查询对象信息。lastId表示上一次查询时最后的id，用于过滤。如果不存在过滤id则可设置为null。
   * pageNum从0开始计算。
   * <p>注意：
   * <ol>
   * <li>如果设置了lastId则表示从lastId的下一个id开始，如果使用不当可能会导致总数totalRows与pageNum
   * 无法对应的bug。如：pageNum=0, lastId=188, 是从189开始，不再是第一页
   * <li>如果pageSize<0，由于当前实现为mysql limit pageNum*pageSize, pageSize，始终会查询所有列信息
   * <li>如果pageNum<0，由于当前实现为mysql limit pageNum*pageSize, pageSize，始终从0开始查询一个pageSize的数据
   * </ol>
   * @param pageSize
   * @param pageNum
   * @param lastId
   * @return
   */
  Collection<T> listPage(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,
          @Param("lastId") Integer lastId);
}
