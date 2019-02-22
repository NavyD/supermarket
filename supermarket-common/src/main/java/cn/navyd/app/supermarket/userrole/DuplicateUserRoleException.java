package cn.navyd.app.supermarket.userrole;

import cn.navyd.app.supermarket.base.DuplicateException;

public class DuplicateUserRoleException extends DuplicateException {
    private static final long serialVersionUID = 5675707415496005634L;
    public DuplicateUserRoleException() {
    }
    
    public DuplicateUserRoleException(String message) {
        super(message);
    }
}
