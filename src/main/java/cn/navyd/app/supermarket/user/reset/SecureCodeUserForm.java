package cn.navyd.app.supermarket.user.reset;

import lombok.Data;

@Data
public class SecureCodeUserForm {
  private Integer id;
  private String code;
  private String newPassword;
}
