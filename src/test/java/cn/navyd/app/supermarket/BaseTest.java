package cn.navyd.app.supermarket;

public class BaseTest {
  /**
   * basedo三个属性
   */
  protected final String[] BASE_PROPERTIES = {"id", "gmtCreate", "gmtModified"};
  
  protected static final String STRING_PREFIX = "_$";
  protected static final String  STRING_SUFFIX= "$_";
  
  /**
   * 使用特殊前后缀组合数据。前后缀共4个字符
   * @param data
   * @return
   */
  protected String getTestData(String data) {
    return STRING_PREFIX + data + STRING_SUFFIX;
  }
}
