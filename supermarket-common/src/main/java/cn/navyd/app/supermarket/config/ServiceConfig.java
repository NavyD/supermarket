package cn.navyd.app.supermarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import cn.navyd.app.supermarket.base.ReadOnlyDao;
import cn.navyd.app.supermarket.config.Qualifiers.EmailForgotSecureCodeServiceQualifier;
import cn.navyd.app.supermarket.config.Qualifiers.EmailRegisterSecureCodeServiceQualifier;
import cn.navyd.app.supermarket.productcategory.ProductCategoryDao;
import cn.navyd.app.supermarket.productcategory.ProductCategoryServiceImpl;
import cn.navyd.app.supermarket.role.RoleDO;
import cn.navyd.app.supermarket.role.RoleDao;
import cn.navyd.app.supermarket.role.RoleService;
import cn.navyd.app.supermarket.role.RoleServiceImpl;
import cn.navyd.app.supermarket.supplier.SupplierDao;
import cn.navyd.app.supermarket.supplier.SupplierServiceImpl;
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

/**
 * 注意：对于使用接口的service实现类，由于使用了spring transactional管理，会使用jdk动态代理，如果使用原始类型注入会报错
 * <pre>
 * &#64;Autowired
 * private UserRoleServiceImpl userRoleServiceImpl
 * </pre>
 * 上面的代码将会异常：
 * <pre>
 *  org.springframework.beans.factory.BeanNotOfRequiredTypeException: Bean named 'userRoleServiceImpl' is expected to be of type 'cn.navyd.app.supermarket.userrole.UserRoleServiceImpl' but was actually of type 'com.sun.proxy.$Proxy70'}
 * </pre>
 * 
 * @author navyd
 *
 */
@Configuration
public class ServiceConfig {
  public UserServiceImpl userServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder,
      UserRoleService userRoleService, RoleService roleService,
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
      ReadOnlyDao<UserDO> userDao, ReadOnlyDao<RoleDO> roleDao) {
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

  @Bean
  public ProductCategoryServiceImpl productCategoryServiceImpl(ProductCategoryDao productCategoryDao) {
    var bean = new ProductCategoryServiceImpl(productCategoryDao);
    return bean;
  }
  
  @Bean
  public SupplierServiceImpl supplierServiceImpl(SupplierDao supplierDao) {
    var bean = new SupplierServiceImpl(supplierDao);
    return bean;
  }


  @Configuration
  static class SecureCodeServiceConfig {
    @Bean
    @EmailForgotSecureCodeServiceQualifier
    public EmailForgotSecureCodeServiceImpl emailForgotPasswordServiceImpl(EmailProperties sender,
        SecureCodeGenerator secureCodeGenerator) {
      EmailForgotSecureCodeServiceImpl bean = new EmailForgotSecureCodeServiceImpl();
      bean.setSecureCodeGenerator(secureCodeGenerator);
      bean.setSender(sender);
      return bean;
    }

    @Bean
    @EmailRegisterSecureCodeServiceQualifier
    public EmailRegisterSecureCodeServiceImpl emailRegisterSecureCodeServiceImpl(
        EmailProperties sender, SecureCodeGenerator secureCodeGenerator) {
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
