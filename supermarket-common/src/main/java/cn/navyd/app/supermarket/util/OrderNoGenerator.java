package cn.navyd.app.supermarket.util;

public interface OrderNoGenerator extends Generator<Long> {
  /**
   * 生成下一个订单编号。
   */
  @Override
  Long next();
}
