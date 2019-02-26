package cn.navyd.app.supermarket.product;

import java.time.LocalDate;
import cn.navyd.app.supermarket.BaseTest;
import cn.navyd.app.supermarket.BasicTestData;
import cn.navyd.app.supermarket.util.EnumUtils;

public class ProductTestData extends BaseTest implements BasicTestData<ProductDO> {
  public static final ProductTestData INSTANCE = new ProductTestData(); 
  
  @Override
  public ProductDO getFirst() {
    var p = new ProductDO();
    p.setName("小龙虾");
    p.setProductionDate(LocalDate.parse("2018-07-06"));
    p.setShelfLife(100);
    p.setProductUnit(EnumUtils.ofSequence(ProductUnitEnum.class, 1).get());
    p.setSpecification(500);
    p.setSpecificationUnit(EnumUtils.ofSequence(SpecificationUnitEnum.class, 2).get());
    p.setProductCategoryId(1);
    p.setSupplierId(1);
    return p;
  }

  @Override
  public int getTotalRows() {
    return 3;
  }

  @Override
  public ProductDO getSavable() {
    var p = new ProductDO();
    p.setName(getTestData("测试商品"));
    p.setProductionDate(LocalDate.parse("2019-02-06"));
    p.setShelfLife(100);
    p.setProductUnit(EnumUtils.ofSequence(ProductUnitEnum.class, 1).get());
    p.setSpecification(500);
    p.setSpecificationUnit(EnumUtils.ofSequence(SpecificationUnitEnum.class, 2).get());
    p.setProductCategoryId(1);
    p.setSupplierId(1);
    return p;
  }

}
