package cn.navyd.app.supermarket.util;

import java.util.Collection;

/**
 * 一个分页信息接口
 * @author navyd
 *
 * @param <T>
 */
public interface PageInfo<T> {
    /**
     * 获取当前页的页码。页码从0开始计算
     * @return
     */
    int getPageNumber();

    /**
     * 获取当前页的分页大小
     * @return
     */
    int getPageSize();

    /**
     * 获取当前分页的总数量
     * @return
     */
    int getTotalRows();
    
    /**
     * 分页数据
     * @return
     */
    Collection<T> getData();
    
    /**
     * 获取总页数。
     * @return
     */
    int getTotalPages();
    
    /**
     * 计算从0到当前页（包括）的总数据量。
     * 注意：如果是最后一页，由于实际数据量很可能不是pageSize，该偏移量代表的是实际的数据量
     * @return
     */
    int getOffset();
    
    default boolean hasNext() {
        return getPageNumber() < getTotalPages();
    }
    
    default boolean hasPrevious() {
        return getPageNumber() > 0;
    }
    
    default boolean isLastPage() {
        return !hasNext();
    }
    
    default boolean isFirstPage() {
        return !hasPrevious();
    }
    
    /**
     * 获取当前页的数据量大小。不同于{@link #getPageSize()},该方法返回的是实际的数据量
     * @return
     */
    default int getSize() {
        return getData().size();
    }
}
