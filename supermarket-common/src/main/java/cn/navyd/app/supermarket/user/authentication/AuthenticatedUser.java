package cn.navyd.app.supermarket.user.authentication;

import java.util.Collection;
import cn.navyd.app.supermarket.user.Authority;
import cn.navyd.app.supermarket.user.User;
import cn.navyd.app.supermarket.user.UserLocker;

public interface AuthenticatedUser extends User, UserLocker {
  Collection<Authority> getAuthorities();
}
