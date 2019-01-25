package cn.navyd.app.supermarket.base;

public interface BaseDao<T extends BaseDO> extends ReadOnlyDao<T> {
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
