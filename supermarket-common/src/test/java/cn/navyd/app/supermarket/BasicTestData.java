package cn.navyd.app.supermarket;

import cn.navyd.app.supermarket.base.BaseDO;

/**
 * 测试数据接口，通过该接口可以返回基本的测试数据
 * @author navyd
 *
 * @param <T>
 */
public interface BasicTestData<T extends BaseDO> {
  /**
   * 返回第一个测试数据，不包含{@link BaseDO}字段。应该可以通过equals()判断，修改返回的对象不影响下次调用
   * @return
   */
  T getFirst();
  
  /**
   * 返回最大行数
   * @return
   */
  int getTotalRows();
  
  /**
   * 返回可保存的对象。要求对象仅{@link BaseDO}字段为null
   * @return
   */
  T getSavable();
  
  /**
   * 获取lastId测试countRowsByLastId，默认返回getTotalRows() - 1
   * @return
   */
  default int getLastId() {
    return getTotalRows() - 1;
  }

  /**
   * 获取第一个对象id。默认id=1
   * @return
   */
  default int getFirstId() {
    return 1;
  }
}
