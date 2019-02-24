package cn.navyd.app.supermarket.productcategory;

import cn.navyd.app.supermarket.base.DuplicateException;

public class DuplicateProductCategoryException extends DuplicateException {
    private static final long serialVersionUID = 5675707415496005634L;
    public DuplicateProductCategoryException() {
    }
    
    public DuplicateProductCategoryException(String message) {
        super(message);
    }
}
