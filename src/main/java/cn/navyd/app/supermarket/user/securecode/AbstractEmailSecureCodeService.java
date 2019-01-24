package cn.navyd.app.supermarket.user.securecode;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import cn.navyd.app.supermarket.user.User;

public abstract class AbstractEmailSecureCodeService implements SecureCodeService<Integer, String> {
  private final Cache<Integer, String> cache;

  public AbstractEmailSecureCodeService() {
    cache = CacheBuilder.newBuilder()
        .maximumSize(getMaximumSize())
        .expireAfterWrite(getDuration())
        .build();
  }

  @Override
  public Optional<String> getCode(Integer key) {
    Objects.requireNonNull(key);
    return Optional.ofNullable(cache.getIfPresent(key));
  }
  
  @Override
  public void sendCode(User user) {
    String code = doSendCode(user);
    cache.put(user.getId(), code);
  }
  
  @Override
  public void removeCode(Integer key) {
    Objects.requireNonNull(key);
    if (getCode(key).isPresent())
      cache.invalidate(key);
  }

  /**
   * code保存时间
   * 
   * @return
   */
  protected Duration getDuration() {
    return Duration.ofMinutes(60);
  }

  /**
   * code最大数量
   * 
   * @return
   */
  protected int getMaximumSize() {
    return Integer.MAX_VALUE;
  }

  /**
   * 发送code并返回该code
   * @param user
   * @return
   */
  protected abstract String doSendCode(User user);
}
