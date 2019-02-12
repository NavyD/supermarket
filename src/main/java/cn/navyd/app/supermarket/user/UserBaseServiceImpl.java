package cn.navyd.app.supermarket.user;

import org.springframework.stereotype.Service;
import cn.navyd.app.supermarket.base.AbstractBaseService;
import cn.navyd.app.supermarket.base.DuplicateException;
import cn.navyd.app.supermarket.base.NotFoundException;
import lombok.Setter;

@Setter
@Service
public class UserBaseServiceImpl extends AbstractBaseService<UserDO> {
  private final UserDao userDao;
  
  public UserBaseServiceImpl(UserDao userDao) {
    super(userDao);
    this.userDao = userDao;
  }

  @Override
  protected void checkAssociativeNotFound(UserDO bean) throws NotFoundException {
  }

  @Override
  protected DuplicateException createDuplicateException(String message) {
    return new DuplicateUserException(message);
  }

  @Override
  protected NotFoundException createNotFoundException(String message) {
    return new UserNotFoundException(message);
  }

}
