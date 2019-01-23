package cn.navyd.app.supermarket.user.securecode;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.springframework.stereotype.Component;

/**
 * 一个简单的安全码生成器。
 * @author navyd
 *
 */
@Component
public class SimpleSecureCodeGenerator implements SecureCodeGenerator {
  private static final int MIN_CODE = 100000, MAX_CODE = 999999;
  
  private static final SecureRandom RANDOM = getSecureRandom();
  
  /**
   * 使用{@link java.security.SecureRandom}生成6位数字code
   */
  @Override
  public String next() {
    final int min = MIN_CODE, max = MAX_CODE;
    int code = -1;
    while ((code = RANDOM.nextInt(max)) < min)
      ;
    return String.valueOf(++code);
  }
  
  private static SecureRandom getSecureRandom() {
    try {
      return SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

}
