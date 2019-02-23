package cn.navyd.app.supermarket.util;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import lombok.Data;

public class PascalToUnderScoresConverterTest {
  PascalToUnderScoresConverter converter = PascalToUnderScoresConverter.getInstance();
  
  @Data
  static class ConverterTest {
    /**
     * 
     */
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 2236160883067719158L;
    private Integer orderNo;
    private Integer id;
    private LocalDate gmtCreateTime;
  }
  
  private static final Collection<String> UNDER_SCORES = Arrays.asList("id", "order_no", "gmt_create_time");
  
  @Test
  void convertTest() {
    Collection<String> names = converter.convert(ConverterTest.class);
    assertThat(names).isNotEmpty()
      .containsAll(UNDER_SCORES)
      .hasSize(UNDER_SCORES.size());
  }
}
