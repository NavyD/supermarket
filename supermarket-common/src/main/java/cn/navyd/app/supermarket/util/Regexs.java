package cn.navyd.app.supermarket.util;

public class Regexs {

  /**
   * 匹配接口default，用于匹配eclipse自动生成空方法
   * 
   * <pre>
   * default ((.|\s)+?\))\s*\{(\s|.)*?\}
   * </pre>
   * 
   * 使用方法：
   * 
   * <pre>
   * 1. 替换default空实现为接口方法：'$1;'
  &#64;Override
  default int countTotalRows() {
    // TODO Auto-generated method stub
    return 0;
  }
  // 替换后
  &#64;Override
  int countTotalRows();
   * </pre>
   */
  public static final String INTERFACE_EMPTY_DEFAULT_METHOD = "default ((.|\\s)+?\\))\\s*\\{(\\s|.)*?\\}";
}
