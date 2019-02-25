package cn.navyd.app.supermarket.supplier;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import cn.navyd.app.supermarket.AbstractBasicDaoTestData;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.BasicDaoTestData;
import cn.navyd.app.supermarket.base.BaseDao;

public class SupplierDaoTest extends BaseDaoTest<SupplierDO> {
  @Autowired
  private SupplierDao supplierDao;
  
  @Test
  public void getByNameTest() {
    var first = data.getFirst();
    assertThat(supplierDao.getByName(first.getName()))
    .isNotNull()
    .hasNoNullFieldsOrProperties()
    .isEqualToIgnoringGivenFields(first, BASE_PROPERTIES);
  }
  
  @Override
  protected BasicDaoTestData<SupplierDO> getBasicDaoTestData() {
    return new AbstractBasicDaoTestData<SupplierDO>(SupplierTestData.INSTANCE) {

      @Override
      public BaseDao<SupplierDO> getBaseDao() {
        return supplierDao;
      }
      
    };
  }
}
