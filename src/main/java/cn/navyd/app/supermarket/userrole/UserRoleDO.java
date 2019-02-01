package cn.navyd.app.supermarket.userrole;

import cn.navyd.app.supermarket.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class UserRoleDO extends BaseDO {
  /**
   * 
   */
  private static final long serialVersionUID = 4641820587368634037L;
  private Integer userId;
  private Integer roleId;
}
