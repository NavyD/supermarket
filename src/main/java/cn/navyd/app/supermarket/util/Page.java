package cn.navyd.app.supermarket.util;

import java.util.Collection; 
import java.util.Collections;

import lombok.NonNull;
import lombok.ToString;

@ToString
public class Page<T> implements PageInfo<T>{
    private final Integer pageSize;
    // 从0开始
    private final Integer pageNumber;
    private final Integer totalRows;
    private final Collection<T> data;
    
    private final int totalPages;
    private final int offset;
    
    public Page(@NonNull Integer pageNumber, @NonNull Integer pageSize, @NonNull Integer totalRows, @NonNull Collection<T> data) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.totalRows = totalRows;
        this.data = data;
        this.totalPages = computeTotalPages(pageSize, totalRows);
        this.offset = computeOffset(pageNumber, pageSize, totalRows, data);
        // 检查分页设置
        checkPageParam();
    }
    
    @Override
    public int getOffset() {
        return offset;
    }
    
    @Override
    public int getTotalPages() {
        return totalPages;
    }
    
    @Override
    public int getTotalRows() {
        return totalRows;
    }
    
    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public Collection<T> getData() {
        return data;
    }
    
    /**
     * 计算总页数。
     * @param pageSize
     * @param totalRows
     * @return
     */
    private static int computeTotalPages(Integer pageSize, Integer totalRows) {
        int totalPages = totalRows / pageSize;
        if (totalPages < 0)
            throw new IllegalArgumentException();
        // 判断是否整除，不整除则再分一页
        if (totalPages * pageSize != totalRows);
            totalPages++;
        return totalPages;
    }
    
    /**
     * 如果是最后一页，则使用data.size+之前页的数量
     * 如果不是，则使用(pageNumber + 1) * pageSize
     * @param pageNumber
     * @param pageSize
     * @param totalRows
     * @param data
     * @return
     */
    private static <T> int computeOffset(Integer pageNumber, Integer pageSize, Integer totalRows, Collection<T> data) {
        // 如果是最后一页
        if ((pageNumber + 1) * pageSize > totalRows) {
            return pageNumber * pageSize + data.size();
        }
        // 不是最后一页
        return (pageNumber + 1) * pageSize;
    }
    
    /**
     * 检查分页设置。必须在所有属性初始化后调用
     * 
     */
    private void checkPageParam() {
        if (pageNumber == null || pageNumber < 0) {
            throw new IllegalArgumentException("illegal pageNumber: " + pageNumber); 
        }
        if (pageSize == null || pageSize <= 0) {
            throw new IllegalArgumentException("illegal pageSize: " + pageSize);
        }
        if (totalRows == null || totalRows < 0)
            throw new IllegalArgumentException("illegal totalRows: " + totalRows);
        if (data.size() > pageSize)
            throw new IllegalArgumentException("illegal data.size: " + data.size());
        // 检查分页数量
        int computedTotalPages = getTotalPages();
        if (computedTotalPages < pageNumber + 1) {
            throw new IllegalArgumentException("参数非法。超过分页总数量。pageNumber: " + pageNumber + ", pageSize:" + pageSize + ", totalRows: " + totalRows);
        }
    }
    
    public static <T> Page<T> of(Integer pageNumber, Integer pageSize, Integer totalRows, Collection<T> data) {
        return new Page<>(pageNumber, pageSize, totalRows, Collections.unmodifiableCollection(data));
    }
}
