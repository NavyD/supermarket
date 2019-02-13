package cn.navyd.app.supermarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import cn.navyd.app.supermarket.base.ReadOnlyDao;
import cn.navyd.app.supermarket.config.Qualifiers.EmailForgotSecureCodeServiceQualifier;
import cn.navyd.app.supermarket.config.Qualifiers.EmailRegisterSecureCodeServiceQualifier;
import cn.navyd.app.supermarket.role.RoleDO;
import cn.navyd.app.supermarket.role.RoleDao;
import cn.navyd.app.supermarket.role.RoleService;
import cn.navyd.app.supermarket.role.RoleServiceImpl;
import cn.navyd.app.supermarket.user.UserDO;
import cn.navyd.app.supermarket.user.UserDao;
import cn.navyd.app.supermarket.user.UserServiceImpl;
import cn.navyd.app.supermarket.user.authentication.EmailRegisterSecureCodeServiceImpl;
import cn.navyd.app.supermarket.user.reset.EmailForgotSecureCodeServiceImpl;
import cn.navyd.app.supermarket.user.securecode.SecureCodeGenerator;
import cn.navyd.app.supermarket.user.securecode.SimpleSecureCodeGenerator;
import cn.navyd.app.supermarket.userrole.UserRoleDao;
import cn.navyd.app.supermarket.userrole.UserRoleService;
import cn.navyd.app.supermarket.userrole.UserRoleServiceImpl;

@Configuration
public class ServiceConfig {
  
  @Bean
  public UserServiceImpl userServiceImpl(
      UserDao userDao,
      PasswordEncoder passwordEncoder,
      UserRoleService userRoleService,
      RoleService roleService,
      EmailRegisterSecureCodeServiceImpl emailRegisterSecureCodeService,
      EmailForgotSecureCodeServiceImpl emailForgotSecureCodeService) {
    UserServiceImpl bean = new UserServiceImpl(userDao);
    bean.setPasswordEncoder(passwordEncoder);
    bean.setUserRoleService(userRoleService);
    bean.setRoleService(roleService);
    bean.setEmailForgotSecureCodeService(emailForgotSecureCodeService);
    bean.setEmailRegisterSecureCodeService(emailRegisterSecureCodeService);
    return bean;
  }
  
  @Bean
  public UserRoleServiceImpl userRoleServiceImpl(UserRoleDao userRoleDao, 
      ReadOnlyDao<UserDO> userDao, 
      ReadOnlyDao<RoleDO> roleDao) {
    UserRoleServiceImpl bean = new UserRoleServiceImpl(userRoleDao);
    bean.setRoleDao(roleDao);
    bean.setUserDao(userDao);
    return bean;
  }
  
  @Bean
  public RoleServiceImpl roleServiceImpl(ReadOnlyDao<UserDO> userDao, RoleDao roleDao) {
    RoleServiceImpl bean = new RoleServiceImpl(roleDao);
    bean.setUserDao(userDao);
    return bean;
  }
  
  
  @Configuration
  class SecureCodeServiceConfig {
    @Bean
    @EmailForgotSecureCodeServiceQualifier
    public EmailForgotSecureCodeServiceImpl emailForgotPasswordServiceImpl(EmailProperties sender, SecureCodeGenerator secureCodeGenerator) {
      EmailForgotSecureCodeServiceImpl bean = new EmailForgotSecureCodeServiceImpl();
      bean.setSecureCodeGenerator(secureCodeGenerator);
      bean.setSender(sender);
      return bean;
    }
    
    @Bean
    @EmailRegisterSecureCodeServiceQualifier
    public EmailRegisterSecureCodeServiceImpl emailRegisterSecureCodeServiceImpl(EmailProperties sender, SecureCodeGenerator secureCodeGenerator) {
      EmailRegisterSecureCodeServiceImpl bean = new EmailRegisterSecureCodeServiceImpl();
      bean.setSecureCodeGenerator(secureCodeGenerator);
      bean.setSender(sender);
      return bean;
    }
    
    @Bean
    public SimpleSecureCodeGenerator simpleSecureCodeGenerator() {
      return new SimpleSecureCodeGenerator();
    }
  }
}
