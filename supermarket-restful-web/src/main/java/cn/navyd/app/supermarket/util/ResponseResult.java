package cn.navyd.app.supermarket.util;

import java.util.Objects;
import lombok.Getter;

/**
 * 后端返回前端的Json对象结构
 * 
 * @author navyd
 *
 * @param <T>
 */
@Getter
public class ResponseResult<T> {
  private final String error;
  private final T data;
  
  public ResponseResult(String error, T data) {
    this.error = error;
    this.data = data;
  }

  /**
   * 返回一个具有error信息, data = null的ResponseResult对象
   * 
   * @param error
   * @return
   */
  public final static <T> ResponseResult<T> ofError(String error) {
    Objects.requireNonNull(error);
    return new ResponseResult<T>(error, null);
  }

  /**
   * 返回一个error=null, 具有data != null 的ResponseResult对象
   * 
   * @param data
   * @return
   */
  public final static <T> ResponseResult<T> ofSuccess(T data) {
    Objects.requireNonNull(data);
    return new ResponseResult<T>(null, data);
  }
}
