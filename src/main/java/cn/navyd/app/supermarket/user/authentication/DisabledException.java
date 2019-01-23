package cn.navyd.app.supermarket.user.authentication;

public class DisabledException extends AuthenticationException {

  /**
   * 
   */
  private static final long serialVersionUID = -7565253820170809871L;
  
  public DisabledException() {
  }
  
  public DisabledException(String message) {
    super(message);
  }

}
