package cn.navyd.app.supermarket.user.authentication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import cn.navyd.app.supermarket.user.User;
import cn.navyd.app.supermarket.user.UserDO;
import cn.navyd.app.supermarket.user.UserLocker;

public class LoginedUser implements User, UserLocker {
  private static final long serialVersionUID = 5833655314479039272L;
  private final UserDO user;
  
  public LoginedUser(UserDO user, Collection<String> authories) {
    this.user = user;
//    this.authories = Collections.unmodifiableCollection(new ArrayList<>(authories));
  }
  
  /**
   * 测试使用，未添加授权
   * @param user
   */
  @Deprecated
  public LoginedUser(UserDO user) {
    this(user, new ArrayList<>());
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public String getPassword() {
    return user.getHashPassword();
  }

  @Override
  public String getEmail() {
    return user.getEmail();
  }

  @Override
  public boolean isEnabled() {
    return user.getEnabled();
  }

  @Override
  public Integer getId() {
    return user.getId();
  }

  @Override
  public int getFailedCount() {
    return user.getFailedCount();
  }

  /**
   * 应该禁止被锁定后修改用户表
   */
  @Override
  public LocalDateTime getLockedTime() {
    return user.getGmtModified();
  }

  @Override
  public String getPhoneNumber() {
    return user.getPhoneNumber();
  }
}
