package cn.navyd.app.supermarket;

import cn.navyd.app.supermarket.base.BaseDO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractBasicDaoTestData<T extends BaseDO> implements BasicDaoTestData<T> {
  private final BasicTestData<T> basicTestData;
  
  @Override
  public T getFirst() {
    return basicTestData.getFirst();
  }

  @Override
  public int getTotalRows() {
    return basicTestData.getTotalRows();
  }

  @Override
  public T getSavable() {
    return basicTestData.getSavable();
  }

}
