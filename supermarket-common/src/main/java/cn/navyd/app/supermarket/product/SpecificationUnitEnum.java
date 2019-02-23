package cn.navyd.app.supermarket.product;

import javax.sound.midi.Sequencer;
import cn.navyd.app.supermarket.util.EnumUtils;
import cn.navyd.app.supermarket.util.EnumUtils.EnumSequencer;
import lombok.Getter;

@Getter
public enum SpecificationUnitEnum implements EnumSequencer {
  // 重量单位
  MICROGRAM(1), GRAM(2), KILOGRAM(3), TONNE(4),
  // 容积单位
  MILLILITER(11), LITER(12),
  // 长度单位
  MILLIMETER(21), CENTIMETER(22), DECIMETRE(23), METER(24), KILOMETER(25);
  
  static {
    EnumUtils.checkUniqueEnumSequencer(SpecificationUnitEnum.class);
  }

  private final int sequence;

  SpecificationUnitEnum(int sequence) {
    this.sequence = sequence;
  }
  
  static <T extends Enum<?> & Sequencer> void sequenceOf(Sequencer sequencer) {
    sequencer.getSequence();
  }
}
