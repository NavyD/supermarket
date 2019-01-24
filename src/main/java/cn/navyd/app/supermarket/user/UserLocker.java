package cn.navyd.app.supermarket.user;

import java.time.LocalDateTime;

/**
 * 该接口表示一个用户锁，定制用户锁定策略
 * @author navyd
 *
 */
public interface UserLocker {
  /**
   * 允许用户无限制的登录失败次数
   */
  public static final int FAILED_COUNT_LIMIT = 5;
  /**
   * 用户限制基本的登录时间 秒
   */
  public static final int BASE_LOGIN_LIMIT_SECOND = 30;
  
  Integer getFailedCount();
  
  /**
   * 获取用户被锁定的时间。在超过限制次数时该方法的返回值应该不变，否则可能导致锁定时间不准确（变长）
   * @return
   */
  LocalDateTime getLockedTime();
  
  /**
   * 如果指定的用户已经被锁定则返回true。
   * <p>默认的锁定策略：
   * <ol>
   * <li>如果用户登录失败次数小于{@link #FAILED_COUNT_LIMIT}，则不会限制登录
   * <li>如果登录失败次数为{@link #FAILED_COUNT_LIMIT}，则限制时间为{@link #BASE_LOGIN_LIMIT_SECOND}
   * <li>如果超过{@link #FAILED_COUNT_LIMIT}，则按照失败次数times*{@link #BASE_LOGIN_LIMIT_SECOND}计算限制时间
   * </ol>
   * <p>注意：如果用户锁定使用user更新failedCount字段时的时间{@link UserDO#getGmtModified()}作为基准，如果用户在锁定期间更新用户字段，将导致锁定时间变更长。
   * 调用者应该保证该用户锁定期间不应该有主动更新用户的权限。
   * @param user
   * @return
   */
  default boolean isLocked() {
    final int failedLimit = FAILED_COUNT_LIMIT, baseLimitSecond = BASE_LOGIN_LIMIT_SECOND;
    final int times = failedLimit - getFailedCount();
    if (times > 0)
      return false;
    final long limitSecond = (times + 1) * baseLimitSecond;
    return getLockedTime().plusSeconds(limitSecond).isAfter(LocalDateTime.now());
  }
  
}
