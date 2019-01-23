package cn.navyd.app.supermarket.role;

import cn.navyd.app.supermarket.base.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 8704720627497214999L;
    public RoleNotFoundException() {
    }
    
    public RoleNotFoundException(String message) {
        super(message);
    }
}
