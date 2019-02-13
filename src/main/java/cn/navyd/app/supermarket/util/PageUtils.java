package cn.navyd.app.supermarket.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Collection;

public class PageUtils {
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
   * 
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
      throw new IllegalArgumentException("参数非法。超过分页总数量。pageNumber: " + pageNumber + ", pageSize:"
          + pageSize + ", totalRows: " + totalRows);
    }
  }

  /**
   * 通过参数计算分页的页数
   * 
   * @deprecated {@link #calculateTotalPages(Integer, Integer)}
   * @param totalRows
   * @param pageSize
   * @return
   */
  @Deprecated
  public static int computeTotalPages(int totalRows, int pageSize) {
    int totalPages = totalRows / pageSize;
    if (totalPages < 0)
      throw new IllegalArgumentException();
    // 判断是否整除，不整除则再分一页
    if (totalPages * pageSize != totalRows)
      ;
    totalPages++;
    return totalPages;
  }

  /**
   * 通过参数计算分页的页数
   * @param totalRows
   * @param pageSize
   * @return
   * @deprecated {@link #getTotalPages(Integer, Integer)}
   */
  @Deprecated
  public static int calculateTotalPages(Integer totalRows, Integer pageSize) {
    return getTotalPages(totalRows, pageSize);
  }

  /**
   * 计算当前分页的大小。通常当前大小为pageSize，只有当在最后一页时返回剩余的实际大小
   * 
   * @param totalRows
   * @param pageSize
   * @param pageNumber
   * @return
   * @deprecated {@link #getCurrentSize(Integer, Integer, Integer)}
   */
  @Deprecated
  public static int calculateCurrentPageSize(Integer totalRows, Integer pageNumber, Integer pageSize) {
    return getCurrentSize(totalRows, pageNumber, pageSize);
  }
  
  /**
   * 如果是最后一页则返回true
   * @param totalRows
   * @param pageNumber
   * @param pageSize
   * @return
   */
  public static boolean isLastPage(Integer totalRows, Integer pageNumber, Integer pageSize) {
    checkPageArgument(totalRows, pageNumber, pageSize);
    return getOffset(pageNumber, pageSize) > totalRows;
  }
  
  /**
   * 计算当前分页的大小。通常当前大小为pageSize，只有当在最后一页时返回剩余的实际大小
   * @param totalRows
   * @param pageNumber
   * @param pageSize
   * @return
   */
  public static int getCurrentSize(Integer totalRows, Integer pageNumber, Integer pageSize) {
    checkPageArgument(totalRows, pageNumber, pageSize);
    int offset = getOffset(pageNumber, pageSize);
    return offset > totalRows ? totalRows + pageSize - offset : pageSize;
  }
  
  /**
   * 获取偏移量。
   * @param pageNumber 从0开始
   * @param pageSize
   * @return
   */
  public static int getOffset(Integer pageNumber, Integer pageSize) {
    checkArgument(pageNumber != null && pageNumber >= 0, "pageNumber: %s", pageNumber);
    checkArgument(pageSize != null && pageSize > 0, "pageSize: %s", pageSize);
    final int offset = (pageNumber + 1) * pageSize;
    if (offset < 0)
      throw new IllegalArgumentException("溢出，参数过大");
    return offset;
  }
  
  /**
   * 通过参数计算分页的页数
   * @param totalRows
   * @param pageSize
   * @return
   */
  public static int getTotalPages(Integer totalRows, Integer pageSize) {
    checkArgument(totalRows != null && totalRows >= 0, "totalRows: %s", totalRows);
    checkArgument(pageSize != null && pageSize > 0, "pageSize: %s", pageSize);
    int totalPages = totalRows / pageSize;
    // 判断是否整除，不整除则再分一页
    if (totalPages * pageSize != totalRows)
      totalPages++;
    return totalPages;
  }
  
  /**
   * 检查参数是否正确
   * <ol>
   * <li>参数是否为null
   * <li>要求{@code totalRows>=0,pageNumber>=0,pageSize>0}
   * <li>分页应该正确。pageNumber不应该超过totalRows与pageSize确定的分页数
   * </ol>
   * 
   * @param totalRows
   * @param pageNumber
   * @param pageSize
   */
  public static void checkPageArgument(Integer totalRows, Integer pageNumber, Integer pageSize) {
    checkArgument(checkNotNull(totalRows) >= 0, "totalRows: %s", totalRows);
    checkArgument(checkNotNull(pageNumber) >= 0, "pageNumber: %s", pageNumber);
    checkArgument(checkNotNull(pageSize) > 0, "pageSize: %s", pageSize);
    checkArgument(pageNumber+1 <= getTotalPages(totalRows, pageSize), "分页范围错误。totalRows:%s, pageNumber:%s, pageSize:%s", totalRows, pageNumber, pageSize);
  }
  
  /**
   * 检查参数是否正确
   * <p>检查data.size与分页参数确定的大小{@link #getCurrentSize(Integer, Integer, Integer)}是否一致
   * @see #checkPageArgument(Integer, Integer, Integer)
   * @param totalRows
   * @param pageNumber
   * @param pageSize
   * @param data
   */
  public static void checkPageArgument(Integer totalRows, Integer pageNumber, Integer pageSize, Collection<?> data) {
    checkPageArgument(totalRows, pageNumber, pageSize);
    int curSize = getCurrentSize(totalRows, pageNumber, pageSize);
    checkArgument(checkNotNull(data).size() == curSize, "实际数据data.size:%s 不符合当前分页数据currentSize:%s", data.size(), curSize);
  }
}
