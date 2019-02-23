package cn.navyd.app.supermarket.product;

import java.time.LocalDate;
import cn.navyd.app.supermarket.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ProductDO extends BaseDO {
  private static final long serialVersionUID = 7788153311679517877L;

  private String name;
  private LocalDate productionDate;
  private Integer shelfLife;
  private ProductUnitEnum productUnit;
  private Integer specification;
  private ProductSpecificationUnitEnum productSpecificationUnit;
  private Integer productCategoryId;
  private String productCategoryName;
  private Integer supplierId;
  private String supplierName;
}
