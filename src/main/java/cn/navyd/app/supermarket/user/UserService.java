package cn.navyd.app.supermarket.user;

import java.util.Optional;

import cn.navyd.app.supermarket.base.BaseService;

public interface UserService extends BaseService<UserDO> {
    /**
     * 通过username获取userDO对象。如果不存在则返回空
     * @param username
     * @return
     */
    Optional<UserDO> getByUsername(String username);
}
