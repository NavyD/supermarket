package cn.navyd.app.supermarket.productcategory;

import cn.navyd.app.supermarket.BaseTest;
import cn.navyd.app.supermarket.BasicTestData;

public class ProductCategoryTestData extends BaseTest implements BasicTestData<ProductCategoryDO> {
  private static final ProductCategoryTestData INSTANCE = new ProductCategoryTestData();
  
  private ProductCategoryTestData() {}
  
  public static ProductCategoryTestData getInstance() {
    return INSTANCE;
  }
  
  @Override
  public ProductCategoryDO getFirst() {
    var first = new ProductCategoryDO();
    first.setId(1);
    first.setParentId(0);
    first.setCategoryName("食品");
    return first;
  }

  @Override
  public int getTotalRows() {
    return 16;
  }

  @Override
  public ProductCategoryDO getSavable() {
    var pc = new ProductCategoryDO();
    pc.setParentId(Integer.MAX_VALUE);
    pc.setCategoryName(getTestData("测试分类"));
    return pc;
  }

}
