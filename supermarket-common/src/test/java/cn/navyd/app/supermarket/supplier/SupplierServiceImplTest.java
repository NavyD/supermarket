package cn.navyd.app.supermarket.supplier;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.AbstractBasicServiceTestData;
import cn.navyd.app.supermarket.BaseServiceTest;
import cn.navyd.app.supermarket.BasicServiceTestData;
import cn.navyd.app.supermarket.base.BaseService;
import cn.navyd.app.supermarket.base.ServiceException;

public class SupplierServiceImplTest extends BaseServiceTest<SupplierDO> {
  @Autowired
  private SupplierService supplierService;
  
  @Transactional
  @Test
  @Override
  public void saveTestForDuplicateException() {
    var savable = data.getSavable();
    savable.setName(data.getFirst().getName());
    assertThatThrownBy(() -> supplierService.save(savable)).isInstanceOf(DuplicateSupplierException.class);
  }

  @Override
  public void saveTestForNotFoundException() {
  }

  @Transactional
  @Test
  @Override
  public void saveTestForOtherException() {
    var savable = data.getSavable();
    savable.setName(data.getOverlengthString());
    assertThatThrownBy(() -> supplierService.save(savable)).isInstanceOf(ServiceException.class);
  }

  @Transactional
  @Test
  @Override
  public void updateByPrimaryKeyTestForDuplicateException() {
    var updatable = data.getSavable();
    updatable.setId(data.getLastId());
    updatable.setName(data.getFirst().getName());
    assertThatThrownBy(() -> supplierService.updateByPrimaryKey(updatable)).isInstanceOf(DuplicateSupplierException.class);
  }

  @Transactional
  @Test
  @Override
  public void updateByPrimaryKeyTestForOtherException() {
    var updatable = data.getFirst();
    updatable.setId(data.getFirstId());
    updatable.setName(data.getOverlengthString());
    assertThatThrownBy(() -> supplierService.updateByPrimaryKey(updatable)).isInstanceOf(ServiceException.class);
  }

  @Override
  protected BasicServiceTestData<SupplierDO> getBasicServiceTestData() {
    return new AbstractBasicServiceTestData<SupplierDO>(SupplierTestData.INSTANCE) {

      @Override
      public BaseService<SupplierDO> getBaseService() {
        return supplierService;
      }
    };
  }
  
  
}
