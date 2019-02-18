package cn.navyd.app.supermarket.product;

import cn.navyd.app.supermarket.util.EnumUtils.EnumSequencer;
import lombok.Getter;

/**
 * 商品单位
 * @author navyd
 *
 */
@Getter
public enum ProductUnitEnum implements EnumSequencer {
  /**
   * 根，支
   */
  STICK(1),
  /**
   * 箱，盒
   */
  BOX(2),
  /**
   * 对
   */
  PAIR(3),
  
  /**
   * 一件
   */
  PIECE(4);
  
  private final int sequence;
  ProductUnitEnum(int sequence) {
    this.sequence = sequence;
  }
}
