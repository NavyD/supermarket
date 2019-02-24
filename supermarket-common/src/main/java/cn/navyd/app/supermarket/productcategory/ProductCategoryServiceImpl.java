package cn.navyd.app.supermarket.productcategory;

import java.util.Collection;
import cn.navyd.app.supermarket.base.AbstractBaseService;
import cn.navyd.app.supermarket.base.DuplicateException;
import cn.navyd.app.supermarket.base.NotFoundException;

public class ProductCategoryServiceImpl extends AbstractBaseService<ProductCategoryDO> implements ProductCategoryService {
  private final ProductCategoryDao productCategoryDao;
  
  public ProductCategoryServiceImpl(ProductCategoryDao productCategoryDao) {
    super(productCategoryDao);
    this.productCategoryDao = productCategoryDao;
  }

  @Override
  public Collection<ProductCategoryDO> listChildrenByPrimaryKey(Integer id) {
    // 检查id 是否存在
    checkNotFoundByPrimaryKey(id);
    return productCategoryDao.listChildrenByPrimaryKey(id);
  }

  @Override
  public Collection<ProductCategoryDO> listDescendantsByPrimaryKey(Integer id) {
    checkNotFoundByPrimaryKey(id);
    return productCategoryDao.listDescendantsByPrimaryKey(id);
  }

  @Override
  protected void checkAssociativeNotFound(ProductCategoryDO bean) throws NotFoundException {
  }
  
  @Override
  protected DuplicateException createDuplicateException(String message) {
    return new DuplicateProductCategoryException(message);
  }
  
  @Override
  protected NotFoundException createNotFoundException(String message) {
    return new ProductCategoryNotFoundException(message);
  }

}
