package cn.navyd.app.supermarket.user.authentication;

import java.util.Optional;
import cn.navyd.app.supermarket.user.securecode.SecureCodeService;

public interface EmailRegisterService extends SecureCodeService<Integer, String> {
  /**
   * 通过user id获取存在的code.
   */
  Optional<String> getCode(Integer id);
}
