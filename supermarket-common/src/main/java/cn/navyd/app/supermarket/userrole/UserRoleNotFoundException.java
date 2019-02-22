package cn.navyd.app.supermarket.userrole;

import cn.navyd.app.supermarket.base.NotFoundException;

public class UserRoleNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 8704720627497214999L;
    public UserRoleNotFoundException() {
    }
    
    public UserRoleNotFoundException(String message) {
        super(message);
    }
}
