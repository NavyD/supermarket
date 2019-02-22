package cn.navyd.app.supermarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.navyd.app.supermarket.user.UserDO;
import cn.navyd.app.supermarket.user.UserDO2UserVOConverter;
import cn.navyd.app.supermarket.user.UserVO;
import cn.navyd.app.supermarket.util.Converter;
import cn.navyd.app.supermarket.util.PageVO.PageInfo2PageVOConverter;

@Configuration
public class ConverterConfig {

    @Bean
    public UserDO2UserVOConverter userDO2UserVOConverter() {
        return new UserDO2UserVOConverter();
    }
    
    @Bean
    public PageInfo2PageVOConverter<UserDO, UserVO> converter(Converter<UserDO, UserVO> converter) {
        return PageInfo2PageVOConverter.of(converter);
    }
}
