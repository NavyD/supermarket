package cn.navyd.app.supermarket;

import cn.navyd.app.supermarket.base.BaseDO;
import cn.navyd.app.supermarket.base.BaseService;

public interface BasicServiceTestData<T extends BaseDO> extends BasicTestData<T> {
  BaseService<T> getBaseService();
}
