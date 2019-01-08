package cn.navyd.app.supermarket.user;

import cn.navyd.app.supermarket.base.DuplicateException;

public class DuplicateUserException extends DuplicateException {
    private static final long serialVersionUID = 5675707415496005634L;
    public DuplicateUserException() {
    }
    
    public DuplicateUserException(String message) {
        super(message);
    }
}
