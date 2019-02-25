package cn.navyd.app.supermarket.supplier;

import cn.navyd.app.supermarket.BaseTest;
import cn.navyd.app.supermarket.BasicTestData;

public class SupplierTestData extends BaseTest implements BasicTestData<SupplierDO> {
  public static final SupplierTestData INSTANCE = new SupplierTestData();

  private SupplierTestData() {

  }

  @Override
  public SupplierDO getFirst() {
    var s = new SupplierDO();
    s.setName("供应商1");
    return s;
  }

  @Override
  public int getTotalRows() {
    return 5;
  }

  @Override
  public SupplierDO getSavable() {
    var s = new SupplierDO();
    s.setName("测试供应商");
    return s;
  }

}
