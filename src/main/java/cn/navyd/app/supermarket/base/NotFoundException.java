package cn.navyd.app.supermarket.base;

public class NotFoundException extends ServiceException {

    /**
     * 
     */
    private static final long serialVersionUID = 4867503569698316797L;
    
    public NotFoundException() {
    }
    
    public NotFoundException(String message) {
        super(message);
    }

}
