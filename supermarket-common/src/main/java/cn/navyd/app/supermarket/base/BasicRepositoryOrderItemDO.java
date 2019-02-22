package cn.navyd.app.supermarket.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class BasicRepositoryOrderItemDO extends BasicOrderItemDO {
  private static final long serialVersionUID = -4586142728084941840L;
  private Integer repositoryId;
}
