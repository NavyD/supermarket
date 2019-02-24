package cn.navyd.app.supermarket.productcategory;

import cn.navyd.app.supermarket.base.NotFoundException;

public class ProductCategoryNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 8704720627497214999L;
    public ProductCategoryNotFoundException() {
    }
    
    public ProductCategoryNotFoundException(String message) {
        super(message);
    }
}
