package cn.navyd.app.supermarket.util;

import javax.annotation.Nonnull;

import lombok.Data;

/**
 * 后端返回前端的Json对象结构
 * 
 * @author navyd
 *
 * @param <T>
 */
@Data
public class ResultVO<T> {
    private final String error;
    private final T data;

    /**
     * 返回一个具有error信息, data = null的ResultVO对象
     * 
     * @param error
     * @return
     */
    public final static <T> ResultVO<T> ofError(@Nonnull String error) {
        return new ResultVO<T>(error, null);
    }

    /**
     * 返回一个error=null, 具有data != null 的ResultVO对象
     * 
     * @param data
     * @return
     */
    public final static <T> ResultVO<T> ofSuccess(@Nonnull T data) {
        return new ResultVO<T>(null, data);
    }
}
