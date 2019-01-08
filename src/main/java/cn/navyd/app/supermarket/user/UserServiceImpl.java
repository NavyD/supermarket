package cn.navyd.app.supermarket.user;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.navyd.app.supermarket.base.AbstractBaseService;
import cn.navyd.app.supermarket.base.DuplicateException;
import cn.navyd.app.supermarket.base.NotFoundException;

@Service
public class UserServiceImpl extends AbstractBaseService<UserDao, UserDO> implements UserService {
    
    @Autowired
    public UserServiceImpl(UserDao dao) {
        super(dao);
    }
     
    @Override
    protected void checkAssociativeNotFound(UserDO bean) throws NotFoundException {
        return;
    }
    
    @Override
    protected DuplicateException createDuplicateException(String message) {
        return new DuplicateUserException(message);
    }
    
    @Override
    protected NotFoundException createNotFoundException(String message) {
        return new UserNotFoundException(message);
    }

    @Override
    public Optional<UserDO> getByUsername(String username) {
        checkUsername(username);
        var user = dao.getByUsername(username);
        return Optional.ofNullable(user);
    }
    
    private void checkUsername(@Nonnull String username) {
        if (username.isEmpty())
            throw new IllegalArgumentException();
    }
}
