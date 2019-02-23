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
  
  /**
   * 匹配Mybatis Generator自动生成的SqlProvider类中的updateByExampleSelective(Map<String, Object>
   * parameter)中的字符串：#{record.id,jdbcType=INTEGER}
   * <pre>
   * (#\{)\w+(\.)
   * 
   * 使用方法：'$1'替换#{record.id,jdbcType=INTEGER} ==> #{id,jdbcType=INTEGER}。
   * </pre>
   */
  public static final String MYBATIS_GENERATOR_SQL_PROVIDER_PARAMETER_PREFIX = "(#\\{)\\w+(\\.)";
}
