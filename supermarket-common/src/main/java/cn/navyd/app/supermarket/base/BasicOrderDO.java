package cn.navyd.app.supermarket.base;

import cn.navyd.app.supermarket.util.EnumUtils.EnumSequencer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class BasicOrderDO<T extends Enum<?> & EnumSequencer> extends BaseDO {

  private static final long serialVersionUID = 2893448310229126746L;
  private Long orderNo;
  private Integer userId;
  private T orderStatus;
  private String remark;
}
