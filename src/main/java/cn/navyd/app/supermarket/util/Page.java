package cn.navyd.app.supermarket.util;

import java.util.Collection;
import java.util.Collections;
import lombok.Data;

@Data
public class Page<T> implements PageInfo<T> {
  private final Integer pageSize;
  // 从0开始
  private final Integer pageNumber;
  private final Integer totalRows;
  private final Collection<T> data;

  private final int totalPages;
  private final int offset;

  public Page(Integer pageNumber, Integer pageSize, Integer totalRows, Collection<T> data) {
    this.pageSize = pageSize;
    this.pageNumber = pageNumber;
    this.totalRows = totalRows;
    this.data = Collections.unmodifiableCollection(data);
    this.totalPages = PageUtils.getTotalPages(totalRows, pageSize);
    this.offset = PageUtils.getOffset(pageNumber, pageSize);
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
}
