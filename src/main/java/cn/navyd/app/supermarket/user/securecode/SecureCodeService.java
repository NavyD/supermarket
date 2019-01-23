package cn.navyd.app.supermarket.user.securecode;

import java.util.Optional;
import cn.navyd.app.supermarket.user.User;

public interface SecureCodeService<K, V> {
  /**
   * 通过key获取{@link #sendCode(User)}一致的code。如果不存在任何code则返回空
   * @param id
   * @return
   */
  Optional<V> getCode(K key);
  
  /**
   * 给指定的user发送代码。通常可以通过user.email或user.phoneNumber确定发送地址
   * @param user
   */
  void sendCode(User user);
}
