package cn.navyd.app.supermarket.user;

import cn.navyd.app.supermarket.base.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 8704720627497214999L;
    public UserNotFoundException() {
    }
    
    public UserNotFoundException(String message) {
        super(message);
    }
}
