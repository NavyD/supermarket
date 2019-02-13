package cn.navyd.app.supermarket.user.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import cn.navyd.app.supermarket.config.EmailProperties;
import cn.navyd.app.supermarket.config.SupermarketProfiles;
import cn.navyd.app.supermarket.user.securecode.SecureCodeGenerator;

@ExtendWith(MockitoExtension.class)
// 使用配置文件中的email 属性
@EnableConfigurationProperties
@SpringBootTest(classes= {EmailProperties.class})
@ActiveProfiles(profiles = SupermarketProfiles.DEVELOPMENT)
public class EmailRegisterServiceImplTest {
  @Mock
  private SecureCodeGenerator generator;
  @Autowired
  private EmailProperties senderEmailProperties;
  private EmailRegisterSecureCodeServiceImpl emailRegisterServiceImpl;
  
  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
    emailRegisterServiceImpl = new EmailRegisterSecureCodeServiceImpl();
    emailRegisterServiceImpl.setSecureCodeGenerator(generator);
    emailRegisterServiceImpl.setSender(senderEmailProperties);
  }
  
  @Test
  void sendCodeTest() {
    final String secureCode = "123456", email = "185352353@qq.com";
    when(generator.next()).thenReturn(secureCode);
    
    assertThat(emailRegisterServiceImpl.getCode(email))
      .isNotPresent();
    
    emailRegisterServiceImpl.sendCode(email);
    
    assertThat(emailRegisterServiceImpl.getCode(email))
      .isPresent()
      .get()
      .isEqualTo(secureCode);
    
    emailRegisterServiceImpl.removeCode(email);
    
    assertThat(emailRegisterServiceImpl.getCode(email))
      .isNotPresent();
    
    verify(generator).next();
  }
}
