package cn.navyd.app.supermarket.util;

import org.junit.Test;

import cn.navyd.app.supermarket.util.SecurityUtils;
import cn.navyd.app.supermarket.util.StringUtils;

public class SecurityUtilsTest {

    @Test
    public void aesTest() {
        String key = "pw";
        String content = "1234";
        System.err.println(SecurityUtils.en(key, content));
    }
}
