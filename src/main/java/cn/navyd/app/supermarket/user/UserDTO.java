package cn.navyd.app.supermarket.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import cn.navyd.app.supermarket.role.RoleDO;
import lombok.Getter;

@Getter
public class UserDTO implements UserDetails, UserLocker {
  /**
   * 
   */
  private static final long serialVersionUID = 1456513707807366263L;
  private final UserDO user;
  private final Collection<RoleDO> roles;
  
  public UserDTO(UserDO user, Collection<RoleDO> roles) {
    super();
    this.user = user;
    this.roles = Collections.unmodifiableCollection(roles);
  }
  
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  @Override
  public String getPassword() {
    return user.getHashPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !isLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.getEnabled();
  }

  @Override
  public Integer getFailedCount() {
    return user.getFailedCount();
  }

  @Override
  public LocalDateTime getLockedTime() {
    return user.getGmtModified();
  }

} 
