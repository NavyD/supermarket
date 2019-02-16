package cn.navyd.app.supermarket.product;

import static com.google.common.base.Preconditions.checkArgument;
import lombok.Getter;

@Getter
public enum ProductWeightUnitEnum {
  MICROGRAM(1), GRAM(2), KILOGRAM(3), TONNE(4);

  private static final int STEP_SCALE = 1000;
  private final int sequence;

  ProductWeightUnitEnum(int sequence) {
    this.sequence = sequence;
  }

  /**
   * 将指定的重量weight与其单位unit转换为当前单位数值
   * @param weight
   * @param unit
   * @return
   */
  public double convert(double weight, ProductWeightUnitEnum unit) {
    checkArgument(weight >= 0 && unit != null);
    final int diff = this.sequence - unit.sequence, scale = STEP_SCALE;
    return diff == 0 ? weight
        : (diff > 0 ? Math.pow(scale, diff) * weight 
            : weight / Math.pow(scale, diff));
  }
  
  /**
   * 将weight gram转换为当前单位
   * @param weight
   * @return
   */
  public double convertGram(double weight) {
    return convert(weight, GRAM);
  }
}
