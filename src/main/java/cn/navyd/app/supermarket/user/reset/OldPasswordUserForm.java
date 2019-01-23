package cn.navyd.app.supermarket.user.reset;

import lombok.Data;

@Data
public class OldPasswordUserForm {
  private Integer id;
  private String oldPassword;
  private String newPassword;
}
