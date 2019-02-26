package cn.navyd.app.supermarket.supplier.record;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import cn.navyd.app.supermarket.AbstractBasicDaoTestData;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.BasicDaoTestData;
import cn.navyd.app.supermarket.base.BaseDao;

public class SupplierRecordDaoTest extends BaseDaoTest<SupplierRecordDO> {
  @Autowired
  private SupplierRecordDao supplierRecordDao;
  
  @Override
  protected BasicDaoTestData<SupplierRecordDO> getBasicDaoTestData() {
    return new AbstractBasicDaoTestData<SupplierRecordDO>(SupplierRecordTestData.INSTANCE) {
      @Override
      public BaseDao<SupplierRecordDO> getBaseDao() {
        return supplierRecordDao;
      }
    };
  }
  
  @Test
  public void getLastByProductIdTest() {
    // 测试数据
    var first = data.getFirst();
    int productId = first.getProductId();
    var last = new SupplierRecordDO();
    last.setProductId(productId);
    last.setSuppliedTime(LocalDate.parse("2018-07-19").atStartOfDay());
    last.setUnitPriceSupply(BigDecimal.valueOf(100.1).setScale(2));
    last.setUnitPriceReturn(BigDecimal.valueOf(90.1).setScale(2));
    
    assertThat(supplierRecordDao.getLastByProductId(productId))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(last, BASE_PROPERTIES);
  }
}
