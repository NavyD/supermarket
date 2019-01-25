package cn.navyd.app.supermarket.config;

import java.io.IOException;
import javax.sql.DataSource;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cn.navyd.app.supermarket.BaseMarker;

@Configuration
@MapperScan(annotationClass=Mapper.class, basePackageClasses=BaseMarker.class)
public class MyBatisConfig { 
    
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // mybatis setting设置
        var config = new org.apache.ibatis.session.Configuration();
        config.setUseGeneratedKeys(true);
        config.setUseColumnLabel(true);
        config.setMapUnderscoreToCamelCase(true);
        bean.setConfiguration(config);
        return bean;
    }
}
