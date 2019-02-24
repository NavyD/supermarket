package cn.navyd.app.supermarket;

import cn.navyd.app.supermarket.base.BaseDO;
import cn.navyd.app.supermarket.base.BaseDao;

public interface BasicDaoTestData<T extends BaseDO> extends BasicTestData<T> {
  BaseDao<T> getBaseDao();
}
