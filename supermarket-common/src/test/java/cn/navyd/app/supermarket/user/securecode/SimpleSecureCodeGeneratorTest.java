package cn.navyd.app.supermarket.user.securecode;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class SimpleSecureCodeGeneratorTest {
  private SimpleSecureCodeGenerator generator = new SimpleSecureCodeGenerator();
  
  @Test
  void nextTest() {
    String code = generator.next();
    assertThat(code)
      .isNotNull()
      .isNotEmpty()
      .hasSize(6);
  }
  
}
