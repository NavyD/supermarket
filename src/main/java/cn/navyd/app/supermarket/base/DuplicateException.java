package cn.navyd.app.supermarket.base;

public class DuplicateException extends ServiceException {

    private static final long serialVersionUID = -8738838075304996626L;
    
    public DuplicateException() {
        
    }
    
    public DuplicateException(String message) {
        super(message);
    }
    
}
