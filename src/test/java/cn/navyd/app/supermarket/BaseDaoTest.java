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
public class BaseDaoTest extends BaseTest {
}
