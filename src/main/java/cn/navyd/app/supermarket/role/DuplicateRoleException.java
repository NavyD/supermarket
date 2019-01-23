package cn.navyd.app.supermarket.role;

import cn.navyd.app.supermarket.base.DuplicateException;

public class DuplicateRoleException extends DuplicateException {
    private static final long serialVersionUID = 5675707415496005634L;
    public DuplicateRoleException() {
    }
    
    public DuplicateRoleException(String message) {
        super(message);
    }
}
