package cn.navyd.app.supermarket.base;

import java.util.Optional;

import cn.navyd.app.supermarket.util.PageInfo;

public interface BaseService<T extends PrimaryKey> {
    /**
     * 通过主键id获取指定对象信息。
     * <p>该方法返回的对象不允许修改，否则将导致缓存异常。
     * @param id
     * @return
     */
    Optional<T> getByPrimaryKey(Integer id);
    
    /**
     * 分页查询对象信息。lastId表示上一次查询时最后的id，用于过滤。如果不存在过滤id则可设置为null
     * <p>pageNum从0开始
     * <p>如果分页参数不合法则抛出异常。
     * @param pageSize
     * @param pageNum
     * @param lastId
     * @return
     */
    PageInfo<T> listPage(Integer pageNum, Integer pageSize, Integer lastId);
    
    /**
     * 保存指定对象。
     * <p>如果关联对象不存在则抛出异常。如果对象已存在则抛出异常
     * <p>所有{@link cn.navyd.app.supermarket.base.BaseDO}中的字段不会被更新，由数据库控制更新
     * @param bean
     */
    T save(T bean) throws ServiceException;
    
    /**
     * 通过对象的主键id更新为其他信息。
     * <p>id将不能被更新。bean中非null部分信息将会被更新，null不会被更新
     * <p>如果bean.id不存在则抛出异常。如果bean的关联对象不存在则抛出异常。如果bean中更新的信息已存在则抛出异常
     * <p>所有{@link cn.navyd.app.supermarket.base.BaseDO}中的字段不会被更新，由数据库控制更新
     * @param bean
     */
    T updateByPrimaryKey(T bean) throws ServiceException;
    
    /**
     * 通过id移除对应的对象信息。
     * 如果id不存在则抛出异常
     * @param id
     */
    void removeByPrimaryKey(Integer id) throws ServiceException;
    
    /**
     * 保存多个bean对象。默认实现调用{@link #save(BaseDO)}。如果需要批量保存操作，建议在sql中实现
     * @param beans
     */
    default void saveList(Iterable<T> beans) throws ServiceException {
        for (T bean : beans)
            save(bean);
    }
    
    /**
     * 分页查询对象信息
     * @param pageSize
     * @param pageNum
     * @return
     */
    default PageInfo<T> listPage(Integer pageNum, Integer pageSize) {
        return listPage(pageNum, pageSize, null);
    }
    
    /**
     * 获取所有对象。
     * <p>该方法仅用于开发测试使用 
     * @return
     */
    default PageInfo<T> listAll() {
        return listPage(Integer.MAX_VALUE, 0);
    }
}
