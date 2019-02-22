package cn.navyd.app.supermarket.user.authentication;

public class IncorrectPasswordException extends AuthenticationException {

  /**
   * 
   */
  private static final long serialVersionUID = -1829073016566280386L;
  
  public IncorrectPasswordException() {
  }
  
  public IncorrectPasswordException(String message) {
    super(message);
  }

}
