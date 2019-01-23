package cn.navyd.app.supermarket.user.authentication;

import lombok.Data;

/**
 * 用户注册时的提交数据对象
 * @author navyd
 *
 */
@Data
public class RegisterUserForm {
  private String username;
  private String password;
  private String iconPath;
  private String email;
  private String phoneNumber;
}
