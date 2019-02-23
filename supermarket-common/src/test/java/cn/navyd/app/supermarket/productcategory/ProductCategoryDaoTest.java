package cn.navyd.app.supermarket.productcategory;

import org.springframework.beans.factory.annotation.Autowired;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.base.BaseDao;

public class ProductCategoryDaoTest extends BaseDaoTest<ProductCategoryDO> {
  @Autowired
  private ProductCategoryDao productCategoryDao;
  
  @Override
  protected BaseDao<ProductCategoryDO> getBaseDao() {
    return productCategoryDao;
  }

  @Override
  protected ProductCategoryDO getFirst() {
    var pc = new ProductCategoryDO();
    pc.setCategoryName("食品");
    pc.setParentId(0);
    return pc;
  }

  @Override
  protected int getTotalRows() {
    return 16;
  }

  @Override
  protected ProductCategoryDO getSavable() {
    var pc = new ProductCategoryDO();
    pc.setCategoryName(getTestData("测试分类"));
    pc.setParentId(getFirstId());
    return pc;
  }

}
