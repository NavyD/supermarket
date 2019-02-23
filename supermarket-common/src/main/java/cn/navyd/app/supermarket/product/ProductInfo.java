package cn.navyd.app.supermarket.product;

import java.time.LocalDate;
import cn.navyd.app.supermarket.base.PrimaryKey;

public interface ProductInfo extends PrimaryKey {
  String getName();
  
  LocalDate getProductionDate();
  
  Integer getShelfLife();
  
  Integer getSpecification();
  
  SpecificationUnitEnum getProductSpecificationUnit();
  
  default boolean isExpired() {
    return getProductionDate()
        .plusDays(getShelfLife())
        .isBefore(LocalDate.now());
  }
}
