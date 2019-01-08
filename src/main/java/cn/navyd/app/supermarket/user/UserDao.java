package cn.navyd.app.supermarket.user;

import org.apache.ibatis.annotations.Mapper;

import cn.navyd.app.supermarket.base.BaseDao; 

@Mapper
public interface UserDao extends BaseDao<UserDO> {
    /**
     * 通过username获取指定的UserDO
     * @return
     */
    UserDO getByUsername(String username);
}