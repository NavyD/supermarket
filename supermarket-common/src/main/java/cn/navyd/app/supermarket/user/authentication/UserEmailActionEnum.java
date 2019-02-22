package cn.navyd.app.supermarket.user.authentication;

public enum UserEmailActionEnum {
  REGISTER("register"), FORGOT_PASSWORD("forgot_password");
  
  private String value;
  
  UserEmailActionEnum(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
}
