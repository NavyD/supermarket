package cn.navyd.app.supermarket;

import cn.navyd.app.supermarket.base.BaseDO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractBasicServiceTestData<T extends BaseDO> implements BasicServiceTestData<T> {
  private final BasicTestData<T> data;
  
  @Override
  public T getFirst() {
    return data.getFirst();
  }
  
  @Override
  public T getSavable() {
    return data.getSavable();
  }
  
  @Override
  public int getTotalRows() {
    return data.getTotalRows();
  }
}
