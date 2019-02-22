package cn.navyd.app.supermarket.util;

import static cn.navyd.app.supermarket.util.PageUtils.getCurrentSize;
import static cn.navyd.app.supermarket.util.PageUtils.getOffset;
import static cn.navyd.app.supermarket.util.PageUtils.getTotalPages;
import static cn.navyd.app.supermarket.util.PageUtils.isLastPage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

public class PageUtilsTest {

  @Test
  void getOffsetTest() {
    assertThat(getOffset(1, 5)).isEqualTo(10);
    assertThatThrownBy(() -> getOffset(-1, 5)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> getOffset(Integer.MAX_VALUE, 5)).isInstanceOf(IllegalArgumentException.class);
  }
  
  @Test
  void getCurrentSizeTest() {
    assertThat(getCurrentSize(11, 2, 3)).isEqualTo(3);
    assertThat(getCurrentSize(11, 3, 3)).isEqualTo(2);
    assertThat(getCurrentSize(11, 0, 22)).isEqualTo(11);
    
    assertThat(getCurrentSize(3, 0, 5)).isEqualTo(3);
    
    assertThat(getCurrentSize(11, 0, Integer.MAX_VALUE)).isEqualTo(11);

    // 偏移量超出
    assertThatThrownBy(() -> getCurrentSize(11, 4, 3)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> getCurrentSize(11, 51, 3))
        .isInstanceOf(IllegalArgumentException.class);

    // 参数错误
    assertThatThrownBy(() -> getCurrentSize(11, -1, 3))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> getCurrentSize(11, 1, -3))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> getCurrentSize(-111, 1, 3))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void isLastPageTest() {
    assertThat(isLastPage(11, 2, 3)).isFalse();
    assertThat(isLastPage(11, 3, 3)).isTrue();
    assertThat(isLastPage(11, 0, 22)).isTrue();
  }
  
  @Test
  void getTotalPagesTest() {
    assertThat(getTotalPages(10, 3)).isEqualTo(4);
  }

}
