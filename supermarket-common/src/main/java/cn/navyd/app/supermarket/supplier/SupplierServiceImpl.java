package cn.navyd.app.supermarket.supplier;

import cn.navyd.app.supermarket.base.AbstractBaseService;
import cn.navyd.app.supermarket.base.DuplicateException;
import cn.navyd.app.supermarket.base.NotFoundException;

public class SupplierServiceImpl extends AbstractBaseService<SupplierDO> implements SupplierService {
//  private final SupplierDao supplierDao;
  
  public SupplierServiceImpl(SupplierDao supplierDao) {
    super(supplierDao);
//    this.supplierDao = supplierDao;
  }

  @Override
  protected void checkAssociativeNotFound(SupplierDO bean) throws NotFoundException {
  }
  
  @Override
  protected DuplicateException createDuplicateException(String message) {
    return new DuplicateSupplierException(message);
  }
  
  @Override
  protected NotFoundException createNotFoundException(String message) {
    return new SupplierNotFoundException(message);
  }

}
