package cn.navyd.app.supermarket.product;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.base.BaseDao;
import cn.navyd.app.supermarket.util.EnumUtils;

public class ProductDaoTest extends BaseDaoTest<ProductDO> {
  @Autowired
  private ProductDao productDao;
  
  @Override
  protected BaseDao<ProductDO> getBaseDao() {
    return productDao;
  }
  
  @Override
  protected ProductDO getFirst() {
    var p = new ProductDO();
    p.setName("小龙虾");
    p.setProductionDate(LocalDate.parse("2018-07-06"));
    p.setShelfLife(100);
    p.setProductUnit(EnumUtils.ofSequence(ProductUnitEnum.class, 1).get());
    p.setSpecification(500);
    p.setSpecificationUnit(EnumUtils.ofSequence(SpecificationUnitEnum.class, 2).get());
    p.setProductCategoryId(1);
    p.setProductCategoryName("食品");
    p.setSupplierId(1);
    p.setSupplierName("供应商1");
    return p;
  }
  
  @Override
  protected int getTotalRows() {
    return 3;
  }
  
  @Override
  protected ProductDO getSavable() {
    var p = new ProductDO();
    p.setName(getTestData("测试商品"));
    p.setProductionDate(LocalDate.parse("2019-02-06"));
    p.setShelfLife(100);
    p.setProductUnit(EnumUtils.ofSequence(ProductUnitEnum.class, 1).get());
    p.setSpecification(500);
    p.setSpecificationUnit(EnumUtils.ofSequence(SpecificationUnitEnum.class, 2).get());
    p.setProductCategoryId(1);
    p.setProductCategoryName("食品");
    p.setSupplierId(1);
    p.setSupplierName("供应商1");
    return p;
  }
}
