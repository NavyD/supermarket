package cn.navyd.app.supermarket.productcategory;

import cn.navyd.app.supermarket.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class ProductCategoryDO extends BaseDO {
  /**
  * 
  */
  private static final long serialVersionUID = -7618655758439414173L;

  private String name;
  private Integer parentId;
}
