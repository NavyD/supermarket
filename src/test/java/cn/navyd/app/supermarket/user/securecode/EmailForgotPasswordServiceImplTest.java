package cn.navyd.app.supermarket.user.securecode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import cn.navyd.app.supermarket.config.EmailProperties;
import cn.navyd.app.supermarket.config.SupermarketProfiles;
import cn.navyd.app.supermarket.user.reset.EmailForgotPasswordServiceImpl;

//使用配置文件中的email 属性
@EnableConfigurationProperties
@SpringBootTest(classes= {EmailProperties.class})
@ActiveProfiles(profiles = SupermarketProfiles.DEVELOPMENT)
public class EmailForgotPasswordServiceImplTest {
  @Mock
  private SecureCodeGenerator generator;
  @Autowired
  private EmailProperties senderEmailProperties;
  private EmailForgotPasswordServiceImpl emailForgotPasswordServiceImpl;
  
  @BeforeEach
  void setup() {
    emailForgotPasswordServiceImpl = new EmailForgotPasswordServiceImpl();
    emailForgotPasswordServiceImpl.setSecureCodeGenerator(generator);
    emailForgotPasswordServiceImpl.setSender(senderEmailProperties);
  }
  
  @Test
  void sendCodeTest() {
    final String secureCode = "123456", email = "185352353@qq.com";
    when(generator.next()).thenReturn(secureCode);
    
    assertThat(emailForgotPasswordServiceImpl.getCode(email))
      .isNotPresent();
    
    emailForgotPasswordServiceImpl.sendCode(email);
    
    assertThat(emailForgotPasswordServiceImpl.getCode(email))
      .isPresent()
      .get()
      .isEqualTo(secureCode);
    
    emailForgotPasswordServiceImpl.removeCode(email);
    
    assertThat(emailForgotPasswordServiceImpl.getCode(email))
      .isNotPresent();
    
    verify(generator).next();
  }
}
