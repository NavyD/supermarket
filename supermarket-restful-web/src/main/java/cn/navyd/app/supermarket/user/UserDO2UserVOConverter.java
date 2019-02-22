package cn.navyd.app.supermarket.user;

import org.springframework.beans.BeanUtils;
import cn.navyd.app.supermarket.util.Converter; 

public class UserDO2UserVOConverter implements Converter<UserDO, UserVO> {

    @Override
    public UserVO convert(UserDO p) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(p, vo);
        return vo;
    }
}
