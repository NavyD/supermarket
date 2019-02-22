package cn.navyd.app.supermarket.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class BasicOrderItemDO extends BaseDO {
  private static final long serialVersionUID = -6795077307208954159L;
  private Integer productId;
  private String productName;
  private Integer quantity;
}
