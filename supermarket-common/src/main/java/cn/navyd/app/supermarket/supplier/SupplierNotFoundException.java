package cn.navyd.app.supermarket.supplier;

import cn.navyd.app.supermarket.base.NotFoundException;

public class SupplierNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 8704720627497214999L;
    public SupplierNotFoundException() {
    }
    
    public SupplierNotFoundException(String message) {
        super(message);
    }
}
