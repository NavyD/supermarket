package cn.navyd.app.supermarket;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import cn.navyd.app.supermarket.config.DaoConfig;
import cn.navyd.app.supermarket.config.DatasourceConfig;
import cn.navyd.app.supermarket.config.MyBatisConfig;
import cn.navyd.app.supermarket.config.SupermarketProfiles;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= {DaoConfig.class, DatasourceConfig.class, MyBatisConfig.class})
@ActiveProfiles(profiles = SupermarketProfiles.DEVELOPMENT)
public class BaseDaoTest {
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
