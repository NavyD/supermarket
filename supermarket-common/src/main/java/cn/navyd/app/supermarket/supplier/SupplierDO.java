package cn.navyd.app.supermarket.supplier;

import cn.navyd.app.supermarket.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SupplierDO extends BaseDO {
  private static final long serialVersionUID = -4650050263198972776L;
  private String name;
}
