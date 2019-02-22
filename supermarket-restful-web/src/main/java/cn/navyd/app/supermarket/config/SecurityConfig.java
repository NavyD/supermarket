package cn.navyd.app.supermarket.config;

import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.navyd.app.supermarket.role.RoleService;
import cn.navyd.app.supermarket.user.UserDO;
import cn.navyd.app.supermarket.user.UserDTO;
import cn.navyd.app.supermarket.user.UserService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private ProviderManager providerManager;
  @Autowired
  private ObjectMapper objectMapper;

  protected void configure(HttpSecurity http) throws Exception {
//    http.csrf().disable().authorizeRequests().anyRequest().authenticated()
//        .antMatchers(HttpMethod.POST, "/login").permitAll().and().formLogin().disable().logout()
//        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "DELETE"))
//        .logoutSuccessHandler((req, resp, auth) -> {
//          resp.setStatus(204);
//          resp.setContentType("application/json");
//          var result = ResponseResult.ofSuccess(auth);
//          objectMapper.writeValue(resp.getOutputStream(), result);
//        }).and().anonymous().disable().rememberMe()
//        .rememberMeServices(tokenBasedRememberMeServices()).key("mykey").and()
//        .addFilterBefore(new RestAuthenticationFilter("/login", objectMapper),
//            UsernamePasswordAuthenticationFilter.class)
//        .addFilterBefore(restUserAuthenticationProcessingFilter(),
//            UsernamePasswordAuthenticationFilter.class)
//        .exceptionHandling().authenticationEntryPoint((req, resp, ex) -> {
//          resp.setStatus(403);
//          resp.setContentType("application/json");
//          var result = ResponseResult.ofError("authenticationEntryPoint 访问被拒绝。" + ex.getMessage());
//          objectMapper.writeValue(resp.getOutputStream(), result);
//        })
//        // 访问失败时调用
//        .accessDeniedHandler((req, resp, ex) -> {
//          var result = ResponseResult.ofError("accessDeniedHandler 访问被拒绝。" + ex.getMessage());
//          objectMapper.writeValue(resp.getOutputStream(), result);
//        });
  }

  @Bean
  public UserDetailsService userDetailsService(UserService userService, RoleService roleService) {
    return username -> {
      Optional<UserDO> user = userService.getByUsername(username);
      if (!user.isPresent())
        throw new UsernameNotFoundException(username);
      UserDetails details = new UserDTO(user.get(), roleService.listByUserId(user.get().getId()));
      return details;
    };
  }

  @Bean
  public ProviderManager providerManager(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider p1 = new DaoAuthenticationProvider();
    p1.setUserDetailsService(userDetailsService);
    ProviderManager providerManager = new ProviderManager(Arrays.asList(p1));
    return providerManager;
  }
}
