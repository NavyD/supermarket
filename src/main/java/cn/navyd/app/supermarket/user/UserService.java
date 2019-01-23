package cn.navyd.app.supermarket.user;

import java.util.Optional;
import cn.navyd.app.supermarket.base.BaseService;
import cn.navyd.app.supermarket.user.authentication.IncorrectPasswordException;
import cn.navyd.app.supermarket.user.authentication.RegisterUserForm;
import cn.navyd.app.supermarket.user.reset.OldPasswordUserForm;
import cn.navyd.app.supermarket.user.reset.SecureCodeUserForm;
import cn.navyd.app.supermarket.user.securecode.IncorrectSecureCodeException;
import cn.navyd.app.supermarket.user.securecode.SecureCodeNotFoundException;

public interface UserService extends BaseService<UserDO> {

    /**
     * 通过username获取userDO对象。如果不存在则返回空
     * @param username
     * @return
     */
    Optional<UserDO> getByUsername(String username);
    
    Optional<UserDO> getByEmail(String email);
    
    /**
     * 注册用户
     * @param registerUser
     * @return
     */
    UserDO register(RegisterUserForm registerUser);
    
    /**
     * 通过用户email找回密码。
     * <p>该方法不会改变任何user数据，仅向user.email发送一个重置密码的邮件。使用邮件重置密码方法：{@link #resetPassword(SecureCodeUserForm)}
     * @param email
     * @return
     */
    void forgotPassword(String email) throws UserNotFoundException;
    
    /**
     * 通过安全代码重置用户密码.如果user.code存在并一致，则更新密码为user.newPassword
     * @throws SecureCodeNotFoundException 如果未找到user.id对应的code
     * @throws IncorrectSecureCodeException 如果code不一致
     * @throws IncorrectPasswordException 如果用户密码不符合要求
     * @param user
     */
    UserDO resetPassword(SecureCodeUserForm user) throws UserNotFoundException, SecureCodeNotFoundException, IncorrectSecureCodeException, IncorrectPasswordException;
    
    /**
     * 通过旧密码重置用户密码。如果user.oldPassword与数据库一致，则更新密码为user.newPassword
     * @throws IncorrectPasswordException 如果用户密码不符合要求
     * @param user
     */
    UserDO resetPassword(OldPasswordUserForm user) throws IncorrectPasswordException;
    
    /**
     * 注册激活。
     * @param id
     * @param code
     * @return
     */
    UserDO enableRegistration(Integer id, String registeredCode);
    
    /**
     * 用户登录操作
     * <ol>
     * <li>如果 未激活{@link UserDO#getEnabled()} 或 被锁定 则抛出异常
     * <li>如果用户密码错误则更新failCount并抛出异常
     * <li>如果登录成功则清除failCount并返回
     * </ol>
     * @param username
     * @param password
     * @return
     */
    UserDO login(String username, String password);
}
