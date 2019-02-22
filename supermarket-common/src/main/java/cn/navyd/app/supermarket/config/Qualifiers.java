package cn.navyd.app.supermarket.config;

import org.springframework.beans.factory.annotation.Qualifier;

public class Qualifiers {
  @Qualifier
  public @interface EmailForgotSecureCodeServiceQualifier {
  }
  
  @Qualifier
  public @interface EmailRegisterSecureCodeServiceQualifier {
  }
}
