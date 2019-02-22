package cn.navyd.app.supermarket.user.securecode;

import java.util.Optional;

public interface SecureCodeService {
  /**
   * 通过address获取code。如果不存在任何code则返回空
   * @param id
   * @return
   */
  Optional<String> getCode(String address);
  
  /**
   * 向指定的地址address发送使用唯一的key关联生成的code。
   * <p>允许多次调用，不管key是否已存在，如果已存在则生成新的code
   * @param key 通常为username
   * @param address 通常为user.email或phonenumber
   */
  void sendCode(String address);
  
  /**
   * 通过key移除存在的code
   * @param key
   */
  void removeCode(String address);
}
