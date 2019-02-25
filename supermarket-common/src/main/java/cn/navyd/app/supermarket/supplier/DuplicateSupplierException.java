package cn.navyd.app.supermarket.supplier;

import cn.navyd.app.supermarket.base.DuplicateException;

public class DuplicateSupplierException extends DuplicateException {
    private static final long serialVersionUID = 5675707415496005634L;
    public DuplicateSupplierException() {
    }
    
    public DuplicateSupplierException(String message) {
        super(message);
    }
}
