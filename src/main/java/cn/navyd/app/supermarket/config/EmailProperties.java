package cn.navyd.app.supermarket.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "email")
@Data
public class EmailProperties {
  private String username;
  private String address;
  private String password;
  private int port;
  private String host;
  private Boolean debug;
  private Integer sessionTimeout;
}
