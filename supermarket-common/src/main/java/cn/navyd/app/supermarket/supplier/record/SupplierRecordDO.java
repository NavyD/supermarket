package cn.navyd.app.supermarket.supplier.record;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import cn.navyd.app.supermarket.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SupplierRecordDO extends BaseDO {
  private static final long serialVersionUID = 508474687851159897L;
  private LocalDateTime suppliedTime;

  private BigDecimal unitPriceSupply;

  private BigDecimal unitPriceReturn;

  private Integer productId;
}
