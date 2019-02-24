package cn.navyd.app.supermarket.productcategory;

import static com.google.common.base.Preconditions.checkArgument;
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
    checkArgument(id != null && id >= 0);
    return productCategoryDao.listChildrenByPrimaryKey(id);
  }

  @Override
  public Collection<ProductCategoryDO> listDescendantsByPrimaryKey(Integer id) {
    checkArgument(id != null && id >= 0);
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
