package cn.navyd.app.supermarket;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import cn.navyd.app.supermarket.config.DaoConfig;
import cn.navyd.app.supermarket.config.DatasourceConfig;
import cn.navyd.app.supermarket.config.EmailProperties;
import cn.navyd.app.supermarket.config.MyBatisConfig;
import cn.navyd.app.supermarket.config.SecurityConfig;
import cn.navyd.app.supermarket.config.ServiceConfig;
import cn.navyd.app.supermarket.config.SupermarketProfiles;

@ActiveProfiles(profiles = SupermarketProfiles.DEVELOPMENT)
@EnableConfigurationProperties
@SpringBootTest(classes= {DaoConfig.class, DatasourceConfig.class, MyBatisConfig.class, ServiceConfig.class, SecurityConfig.class, EmailProperties.class})
public class BaseServiceTest extends BaseTest {
}
