package cn.navyd.app.supermarket.user.securecode;

import cn.navyd.app.supermarket.util.Generator;

public interface SecureCodeGenerator extends Generator<String> {

  /**
   * 返回一个安全码。要求返回的安全码不应该重复
   */
  String next();
}
