package cn.navyd.app.supermarket.util;

import java.util.Collection;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class PageVO<T> {
    // 当前分页 页码
    private Integer pageNumber;
    // 分页 大小 行数
    private Integer pageSize;
    // 当前分页 行数
    private Integer currentSize;
    // 总记录数
    private Integer totalRows;
    // 总页数
    private Integer totalPages;
    // 分页数据
    private Collection<T> data;
    
    @RequiredArgsConstructor
    public static class PageInfo2PageVOConverter<T, R> implements Converter<PageInfo<T>, PageVO<R>> {
        private final Converter<T, R> converter;
        
        @Override
        public PageVO<R> convert(PageInfo<T> page) {
            PageVO<R> vo = new PageVO<>();
            vo.setPageNumber(page.getPageNumber());
            vo.setPageSize(page.getPageSize());
            vo.setCurrentSize(page.getSize());
            vo.setTotalRows(page.getTotalRows());
            vo.setTotalPages(page.getTotalPages());
            vo.setData(converter.convertAll(page.getData()));
            return vo;
        }
        
        public static <T, R> PageInfo2PageVOConverter<T, R> of(Converter<T, R> converter) {
            return new PageInfo2PageVOConverter<>(converter);
        }
    }
}
