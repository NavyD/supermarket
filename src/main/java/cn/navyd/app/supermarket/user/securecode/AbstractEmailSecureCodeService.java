package cn.navyd.app.supermarket.user.securecode;

import static com.google.common.base.Preconditions.checkNotNull;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public abstract class AbstractEmailSecureCodeService implements SecureCodeService<String> {
  private final Cache<String, String> cache;

  public AbstractEmailSecureCodeService() {
    cache = CacheBuilder.newBuilder()
        .maximumSize(getMaximumSize())
        .expireAfterWrite(getDuration())
        .build();
  }

  @Override
  public Optional<String> getCode(String address) {
    Objects.requireNonNull(address);
    return Optional.ofNullable(cache.getIfPresent(address));
  }
  
  @Override
  public void sendCode(String address) {
    checkNotNull(address);
    String code = doSendCode(address);
    cache.put(address, code);
  }
  
  @Override
  public void removeCode(String address) {
    Objects.requireNonNull(address);
    if (getCode(address).isPresent())
      cache.invalidate(address);
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
   * @param username
   * @param emailAddress
   * @return
   */
  protected abstract String doSendCode(String emailAddress);
}
