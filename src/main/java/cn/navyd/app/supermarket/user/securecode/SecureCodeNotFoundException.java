package cn.navyd.app.supermarket.user.securecode;

import cn.navyd.app.supermarket.base.NotFoundException;

public class SecureCodeNotFoundException extends NotFoundException {
  /**
   * 
   */
  private static final long serialVersionUID = -8578298701687903669L;

  public SecureCodeNotFoundException() {
  }
  
  public SecureCodeNotFoundException(String message) {
      super(message);
  }
}
