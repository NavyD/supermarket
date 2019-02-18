package cn.navyd.app.supermarket.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

/**
 * 一个简单的{@link Observable}实现
 * @author navyd
 *
 * @param <T>
 */
public class SimpleObservable<T> implements Observable<T> {
  private final Collection<Observer<T>> observers;
  private boolean changed = false;
  
  public SimpleObservable() {
    observers = new HashSet<>(10);
  }
  
  public SimpleObservable(Collection<Observer<T>> observers) {
    this.observers = new HashSet<>(observers);
  }
  
  /**
   * 返回一个不可修改的observers。
   * @return
   */
  protected Collection<Observer<T>> getObservers() {
    return Collections.unmodifiableCollection(observers);
  }
  
  @Override
  public Observable<T> changed() {
    changed = true;
    return this;
  }
  
  @Override
  public boolean hasChanged() {
    return changed;
  }
  
  @Override
  public Observable<T> register(Observer<T> observer) {
    Objects.requireNonNull(observer);
    observers.add(observer);
    return this;
  }
  
  @Override
  public Observable<T> remove(Observer<T> observer) {
    Objects.requireNonNull(observer);
    if (!observers.remove(observer))
      throw new IllegalArgumentException("移除失败。Observer: " + observer);
    return this;
  }

  @Override
  public Observable<T> notifyObservers(T original, T updated) {
    if (changed) {
      changed = false;
      observers.forEach(observer -> observer.update(original, updated));
    }
    return this;
  }
}
