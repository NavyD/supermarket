package cn.navyd.app.supermarket.product;

import org.springframework.beans.factory.annotation.Autowired;
import cn.navyd.app.supermarket.AbstractBasicDaoTestData;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.BasicDaoTestData;
import cn.navyd.app.supermarket.base.BaseDao;

public class ProductDaoTest extends BaseDaoTest<ProductDO> {
  @Autowired
  private ProductDao productDao;
  
  @Override
  protected BasicDaoTestData<ProductDO> getBasicDaoTestData() {
    return new AbstractBasicDaoTestData<ProductDO>(ProductTestData.INSTANCE) {
      @Override
      public BaseDao<ProductDO> getBaseDao() {
        return productDao;
      }
    };
  }
}
