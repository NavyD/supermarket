package cn.navyd.app.supermarket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BaseMockTest extends BaseTest {
  @BeforeEach
  protected void init() {
    MockitoAnnotations.initMocks(this);
  }
}
