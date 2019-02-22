package cn.navyd.app.supermarket.util;

public class Regexs {

  /**
   * 匹配接口default，用于匹配eclipse自动生成空方法
   * <pre>default ((.|\s)+?\))\s*\{(\s|.)*?\}</pre>
   * 
   */
  public static final String INTERFACE_EMPTY_DEFAULT_METHOD = "default ((.|\\s)+?\\))\\s*\\{(\\s|.)*?\\}";
}
