package cn.navyd.app.supermarket.user;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.apache.commons.lang3.ArrayUtils;
import static com.google.common.base.Preconditions.*;
import cn.navyd.app.supermarket.base.BaseService;
import cn.navyd.app.supermarket.role.RoleDO;
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
     * 注册用户。
     * <p>要求使用安全码注册，通常应该先调用{@link #sendRegisteringCodeByEmail(String)}缓存了对应的安全码并传入验证正确即可注册
     * @param registerUser
     * @return
     */
    UserDO register(RegisterUserForm registerUser);
    
    /**
     * 发送注册码。如果email已被使用则抛出异常
     * @param registerUser
     * @return
     */
    void sendRegisteringCodeByEmail(String email);
    
    /**
     * 通过用户email发送安全码找回密码
     * <p>向user.email发送一个重置密码的邮件。使用邮件重置密码方法：{@link #resetPassword(SecureCodeUserForm)}
     * @param email
     * @return
     */
    void sendForgotPasswordCodeByEmail(String email) throws UserNotFoundException;
    
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
    
    /**
     * 向指定用户userId添加角色信息roleIds，并返回该用户添加完成后所有的角色信息
     * @param userId
     * @param roleIds
     * @return
     */
    Collection<RoleDO> addRoles(Integer userId, Collection<Integer> roleIds);
    
    default Collection<RoleDO> addRoles(Integer userId, Integer... roleIds) {
      checkNotNull(userId);
      checkArgument(!ArrayUtils.isEmpty(roleIds), "roleIds为空");
      return addRoles(userId, Arrays.asList(roleIds));
    }
    
    /**
     * 对指定用户移除角色信息并返回移除后的该用户关联的角色信息
     * @param userId
     * @param roleIds
     * @return
     */
    Collection<RoleDO> removeRoles(Integer userId, Collection<Integer> roleIds);
    
    default Collection<RoleDO> removeRoles(Integer userId, Integer... roleIds) {
      checkNotNull(userId);
      checkArgument(!ArrayUtils.isEmpty(roleIds), "roleIds为空");
      return removeRoles(userId, Arrays.asList(roleIds));
    }
}
