package cn.navyd.app.supermarket.util;

public class PageUtil {
    public static final String PAGE_NUMBER_MIN_VALUE = "0";
    
    public static final String PAGE_SIZE_DEFAULT_VALUE = "15";
    
    public static final Integer PAGE_NUMBER_MIN = Integer.parseInt(PAGE_NUMBER_MIN_VALUE);
    
    public static final Integer PAGE_SIZE_MIN = 5;
    
    public static final Integer PAGE_SIZE_DEFAULT = Integer.parseInt(PAGE_SIZE_DEFAULT_VALUE);
    
    public static final Integer PAGE_SIZE_MAX = 200;
    
    
    public static final String PARAM_PAGE_NUMBER = "page_num";
    
    public static final String PARAM_PAGE_SIZE = "page_size";
    
    public static final String PARAM_LAST_ID = "last_id";
    
    /**
     * 检查分页page参数是否合法。
     * <ol>
     * <li>如果pageNumber==null或小于 {@link #PAGE_NUMBER_MIN}则异常
     * <li>如果pageSize==null或小于{@link #PAGE_SIZE_MIN}或大于{@link #PAGE_SIZE_MAX}则异常
     * <li>如果通过totalRows与pageSize计算的页数与pageNumber不符合则异常。{@link #computeTotalPages(int, int)}
     * </ol>
     * @param pageNumber
     * @param pageSize
     * @param totalRows
     */
    public static void checkPageParam(Integer pageNumber, Integer pageSize, Integer totalRows) {
        if (pageNumber == null || pageNumber < PAGE_NUMBER_MIN) {
            throw new IllegalArgumentException("illegal pageNumber: " + pageNumber); 
        }
        if (pageSize == null || (pageSize < PAGE_SIZE_MIN || pageSize > PAGE_SIZE_MAX)) {
            throw new IllegalArgumentException("illegal pageSize: " + pageSize);
        }
        // 检查分页数量
        int computedTotalPages = computeTotalPages(totalRows, pageSize);
        if (computedTotalPages < pageNumber + 1) {
            throw new IllegalArgumentException("参数非法。超过分页总数量。pageNumber: " + pageNumber + ", pageSize:" + pageSize + ", totalRows: " + totalRows);
        }
    }
    
    /**
     * 通过参数计算分页的页数
     * @param totalRows
     * @param pageSize
     * @return
     */
    public static int computeTotalPages(int totalRows, int pageSize) {
        int totalPages = totalRows / pageSize;
        if (totalPages < 0)
            throw new IllegalArgumentException();
        // 判断是否整除，不整除则再分一页
        if (totalPages * pageSize != totalRows);
            totalPages++;
        return totalPages;
    }
}
