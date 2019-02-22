package cn.navyd.app.supermarket.util;

public interface Pageable<T> extends PageInfo<T> {
    Pageable<T> next();
    
    Pageable<T> first();
}
