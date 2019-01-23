package cn.navyd.app.supermarket.user.authentication;

public class LockedException extends AuthenticationException {
  /**
   * 
   */
  private static final long serialVersionUID = -6640246404875736802L;

  public LockedException() {
  }
  
  public LockedException(String message) {
    super(message);
  }
}
