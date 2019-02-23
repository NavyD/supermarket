package cn.navyd.app.supermarket.productcategory;

import cn.navyd.app.supermarket.base.BaseDO;

public class ProductCategoryDO extends BaseDO {
  /**
  * 
  */
  private static final long serialVersionUID = -7618655758439414173L;

  private String categoryName;

  private Integer parentId;

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public Integer getParentId() {
    return parentId;
  }

  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }
}
