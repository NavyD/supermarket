package cn.navyd.app.supermarket.product;

public enum OrderTypeEnum {
  /**
   * 进货
   */
  PURCHASE,
  /**
   * 进货退货
   */
  PURCHASE_RETURN,
  /**
   * 销售
   */
  SALE,
  /**
   * 销售退货
   */
  SALE_RETURN,
  /**
   * 上下架
   */
  SHELF,
  /**
   * 库存转移。转库
   */
  TRANSFER;
}
