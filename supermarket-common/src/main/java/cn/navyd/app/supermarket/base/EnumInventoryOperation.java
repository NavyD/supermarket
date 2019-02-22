package cn.navyd.app.supermarket.base;

import cn.navyd.app.supermarket.util.EnumUtils.EnumSequencer;

public interface EnumInventoryOperation extends EnumSequencer {
  /**
   * 如果库存操作是出库则返回true。否则返回false表示入库操作
   * @return
   */
  boolean isOutbound();
}
