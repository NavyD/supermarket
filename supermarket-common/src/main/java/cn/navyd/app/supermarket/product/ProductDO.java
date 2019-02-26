package cn.navyd.app.supermarket.product;

import java.time.LocalDate;
import cn.navyd.app.supermarket.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class ProductDO extends BaseDO {
  private static final long serialVersionUID = 7788153311679517877L;
  
  private String name;

  private LocalDate productionDate;

  private Integer shelfLife;

  private ProductUnitEnum productUnit;

  private Integer specification;

  private SpecificationUnitEnum specificationUnit;

  private Integer productCategoryId;

  private Integer supplierId;
}
