package cn.navyd.app.supermarket.user;

import cn.navyd.app.supermarket.base.BaseDO;
import cn.navyd.app.supermarket.role.RoleVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UserVO extends BaseDO {
    /**
     * 
     */
    private static final long serialVersionUID = 1197268587172778364L;

    private String username;

    private String iconPath;

    private String email;

    private Boolean enabled;
    
    private String phoneNumber;
    
    private RoleVO role;
}
