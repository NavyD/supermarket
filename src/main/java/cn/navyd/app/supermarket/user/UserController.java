package cn.navyd.app.supermarket.user;

import static cn.navyd.app.supermarket.util.PageUtil.PAGE_NUMBER_MIN_VALUE;
import static cn.navyd.app.supermarket.util.PageUtil.PAGE_SIZE_DEFAULT_VALUE;
import static cn.navyd.app.supermarket.util.PageUtil.PARAM_LAST_ID;
import static cn.navyd.app.supermarket.util.PageUtil.PARAM_PAGE_NUMBER;
import static cn.navyd.app.supermarket.util.PageUtil.PARAM_PAGE_SIZE;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.navyd.app.supermarket.util.Converter;
import cn.navyd.app.supermarket.util.PageInfo;
import cn.navyd.app.supermarket.util.PageVO;
import cn.navyd.app.supermarket.util.RestApiConsts;
import cn.navyd.app.supermarket.util.ResultVO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(path=RestApiConsts.USER, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {
    private final Converter<UserDO, UserVO> userDo2UserVoConverter;
    private final UserService userService;
    private final Converter<PageInfo<UserDO>, PageVO<UserVO>> pageInfo2PageVoConverter;
    
    @GetMapping
    public ResultVO<PageVO<UserVO>> getAllUsersByPage(
            @RequestParam(name=PARAM_PAGE_NUMBER, defaultValue=PAGE_NUMBER_MIN_VALUE) Integer pageNumber, 
            @RequestParam(name=PARAM_PAGE_SIZE, defaultValue=PAGE_SIZE_DEFAULT_VALUE) Integer pageSize, 
            @RequestParam(name=PARAM_LAST_ID, required=false) Integer lastUserId) {
        PageInfo<UserDO> userDoPage = userService.listPage(pageNumber, pageSize, lastUserId);
        PageVO<UserVO> data = pageInfo2PageVoConverter.convert(userDoPage);
        return ResultVO.ofSuccess(data);
    }
    
    @GetMapping("/{userId}")
    public ResultVO<UserVO> getUserById(@PathVariable Integer userId) {
        Optional<UserDO> user = userService.getByPrimaryKey(userId);
        if (!user.isPresent())
            throw new UserNotFoundException("userId: " + userId);
        UserVO data = userDo2UserVoConverter.convert(user.get());
        return ResultVO.ofSuccess(data);
    }

}
