package cn.navyd.app.supermarket.role;

import org.springframework.security.core.GrantedAuthority;
import cn.navyd.app.supermarket.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class RoleDO extends BaseDO implements GrantedAuthority {

    /**
     * 
     */
    private static final long serialVersionUID = -4144152430100608516L;
    
    private String name;
    private Boolean enabled;
    private Integer parentId;
    @Override
    public String getAuthority() {
      return name;
    }
}
