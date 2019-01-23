package cn.navyd.app.supermarket.user.authentication;

public class AuthenticationException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -7834951725510484573L;
  
  public AuthenticationException() {
  }
  
  public AuthenticationException(String message) {
    super(message);
  }

}
