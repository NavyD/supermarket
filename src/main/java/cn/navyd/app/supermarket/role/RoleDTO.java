package cn.navyd.app.supermarket.role;

import org.springframework.security.core.GrantedAuthority;
import cn.navyd.app.supermarket.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class RoleDTO extends BaseDO implements GrantedAuthority {
  /**
   * 
   */
  private static final long serialVersionUID = 7442069759599827330L;
  private String name;
  private Boolean enabled;
  private RoleDTO parentRole; 
  
  @Override
  public String getAuthority() {
    return name;
  }
}
