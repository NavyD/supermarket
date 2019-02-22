package cn.navyd.app.supermarket.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

@FunctionalInterface
public interface Converter<P, R> {
    /**
     * 将指定的类型p转换为对象r
     * @param p
     * @return
     */
    R convert(P p);
    
    /**
     * 批量转换。该方法的实现直接在循环调用{@link #convert(Object)}。
     * @param c
     * @return
     */
    default Collection<R> convertAll(Collection<P> c) {
        final int size = c.size();
        // 如果集合c.size()过大则 使用链表list
        final Collection<R> result = size < 800 ? new ArrayList<>(size) : new LinkedList<>();
        for (P p : c) {
            result.add(convert(p));
        }
        return result;
    }
}
