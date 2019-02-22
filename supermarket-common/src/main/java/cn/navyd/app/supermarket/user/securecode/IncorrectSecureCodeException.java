package cn.navyd.app.supermarket.user.securecode;

import cn.navyd.app.supermarket.base.ServiceException;

public class IncorrectSecureCodeException extends ServiceException {
  /**
   * 
   */
  private static final long serialVersionUID = 5880055408897535596L;

  public IncorrectSecureCodeException() {
  }

  public IncorrectSecureCodeException(String message) {
      super(message);
  }
}
