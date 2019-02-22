package cn.navyd.app.supermarket.util;

public interface Generator<T> {
  /**
   * 获取下一个值。
   * @return
   */
  T next();
}
