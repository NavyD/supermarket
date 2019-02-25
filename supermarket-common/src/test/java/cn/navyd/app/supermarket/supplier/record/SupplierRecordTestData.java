package cn.navyd.app.supermarket.supplier.record;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import cn.navyd.app.supermarket.BasicTestData;

public class SupplierRecordTestData implements BasicTestData<SupplierRecordDO> {
  public static final SupplierRecordTestData INSTANCE = new SupplierRecordTestData();
  
  private SupplierRecordTestData() {
    
  }
  
  @Override
  public SupplierRecordDO getFirst() {
    var sr = new SupplierRecordDO();
    sr.setSuppliedTime(LocalDate.parse("2018-07-10").atStartOfDay());
    sr.setUnitPriceSupply(BigDecimal.valueOf(101.11));
    sr.setUnitPriceReturn(BigDecimal.valueOf(98).setScale(2));
    sr.setProductId(1);
    sr.setProductName("小龙虾");
    return sr;
  }

  @Override
  public int getTotalRows() {
    return 4;
  }

  @Override
  public SupplierRecordDO getSavable() {
    var sr = new SupplierRecordDO();
    sr.setSuppliedTime(LocalDateTime.now());
    sr.setUnitPriceSupply(BigDecimal.valueOf(100.01));
    sr.setUnitPriceReturn(BigDecimal.valueOf(88.88));
    sr.setProductId(1);
    sr.setProductName("小龙虾");
    return sr;
  }

}
