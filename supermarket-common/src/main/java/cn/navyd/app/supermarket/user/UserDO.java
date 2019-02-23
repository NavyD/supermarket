package cn.navyd.app.supermarket.user;

import cn.navyd.app.supermarket.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class UserDO extends BaseDO {
    /**
     * 
     */
    private static final long serialVersionUID = -4338333825735075853L;
    private String username;
    private String hashPassword;
    private String email;
    private Boolean enabled;
    private String phoneNumber;
    private Integer failedCount;
}