package cn.navyd.app.supermarket.user;

/**
 * 该接口表示一个用户锁，定制用户锁定策略
 * @author navyd
 *
 */
public interface UserLocker {
  
  /**
   * 如果指定的用户已经被锁定则返回true。
   * @return
   */
  boolean isLocked();
  
}
