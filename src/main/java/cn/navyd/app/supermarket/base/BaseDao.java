package cn.navyd.app.supermarket.base;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

public interface BaseDao<T extends PrimaryKey> {
    /**
     * 获取存在总数量
     * @return
     */
    int countTotalRows();
    
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
    
    /**
     * 保存指定对象到数据库。
     * <p>该方法保存除BaseDO的字段的属性，其中的字段由数据库控制。对于存在默认值的字段，当置为null保存时，
     * 由数据库使用默认值保存
     * @param bean
     */
    void save(T bean);
    
    /**
     * 通过对象的主键BaseDO.id更新其他字段的信息。
     * <p>id将不能被更新。由于采用动态sql的方式，对于允许使用null的字段将不能再次被null。
     * @param bean
     */
    void updateByPrimaryKey(T bean);
    
    /**
     * 通过主键从数据库中移除对象信息。
     * @param id
     */
    void removeByPrimaryKey(Integer id);
}
