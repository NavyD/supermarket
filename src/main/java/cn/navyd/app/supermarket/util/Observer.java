package cn.navyd.app.supermarket.util;

/**
 * 表示一个观察者，观察指定资源的改变并得到通知。
 * @see Observable
 * @author navyd
 *
 * @param <T>
 */
public interface Observer<T> {
  /**
   * 当指定资源被改变时将会被调用。将会传递原始资源与以改变的资源。
   * @see Observable#notifyObservers(Object, Object)
   */
  void update(T original, T updated);
  
  default void update(T updated) {
    update(null, updated);
  }
}
