package cn.navyd.app.supermarket.util;

/**
 * 观察者模式。被观察的资源应该实现的接口，通过该接口可以在实现类资源被改变时通知观察者
 * @see Observer
 * @author navyd
 *
 * @param <T>
 */
public interface Observable<T> {
  /**
   * 表示数据已经被改变，表示准备好通知观察者
   * @return
   */
  Observable<T> changed();
  
  /**
   * 如果数据已经被改变则返回true
   * @return
   */
  boolean hasChanged();
  
  /**
   * 注册指定的Observer观察该资源的变化。
   * <p>对同一个引用多次register调用则不会影响notifyObservers()的调用。
   * 
   * @param observer
   * @return
   */
  Observable<T> register(Observer<T> observer);
  
  /**
   * 移除指定的Observer，不再观察该资源的变化。
   * <p>如果移除失败则会抛出异常
   * @param observer
   * @return
   */
  Observable<T> remove(Observer<T> observer) throws IllegalArgumentException;
  
  /**
   * 通知所有注册的Observer，并将原始资源original和已更新的资源updated传递过去。
   * @see Observer#update(Object)
   * @param original
   * @param updated
   * @return
   */
  Observable<T> notifyObservers(T original, T updated);
  
  /**
   * 通知所有注册的Observer，并将变化的资源传递过去。
   * @see Observer#update(Object)
   * @param t
   * @return
   */
  default Observable<T> notifyObservers(T updated) {
    return notifyObservers(null, updated);
  }
  
  default Observable<T> notifyObservers() {
    return notifyObservers(null);
  }
}
