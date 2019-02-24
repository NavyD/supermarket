package cn.navyd.app.supermarket.productcategory;

import java.util.Collection;
import cn.navyd.app.supermarket.base.BaseService;

public interface ProductCategoryService extends BaseService<ProductCategoryDO> {
  Collection<ProductCategoryDO> listChildrenByPrimaryKey(Integer id);
  
  Collection<ProductCategoryDO> listDescendantsByPrimaryKey(Integer id);
}
