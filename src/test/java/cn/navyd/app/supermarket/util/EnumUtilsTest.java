package cn.navyd.app.supermarket.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import cn.navyd.app.supermarket.util.EnumUtils.EnumSequencer;
import lombok.Getter;

public class EnumUtilsTest {

  @Test
  void hasRepeatedSequenceTest() {
    assertThat(EnumUtils.hasRepeatedSequence(RepeatedSequencer.class)).isTrue();
    assertThat(EnumUtils.hasRepeatedSequence(NonRepeatedSequencer.class)).isFalse();
  }

  @Test
  void checkUniqueSequencerTest() {
    assertThatThrownBy(() -> EnumUtils.checkUniqueEnumSequencer(RepeatedSequencer.class))
        .isInstanceOf(IllegalArgumentException.class);
    EnumUtils.checkUniqueEnumSequencer(NonRepeatedSequencer.class);
  }

  @Getter
  private static enum RepeatedSequencer implements EnumSequencer {
    FIRST(1), SECOND(2), FIRST_REPEATED(1);
    private final int sequence;

    RepeatedSequencer(int sequence) {
      this.sequence = sequence;
    }
  }

  @Getter
  private static enum NonRepeatedSequencer implements EnumSequencer {
    FIRST(1), SECOND(2);
    private final int sequence;

    NonRepeatedSequencer(int sequence) {
      this.sequence = sequence;
    }
  }
}
