package cn.navyd.app.supermarket.base;

public class ServiceException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 8594232858179379204L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }
}
