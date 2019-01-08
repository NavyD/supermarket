package cn.navyd.app.supermarket.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import cn.navyd.app.supermarket.BaseMarker;
import cn.navyd.app.supermarket.base.BaseDO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@MapperScan(annotationClass=Mapper.class, basePackageClasses=BaseMarker.class)
public class MyBatisConfig { 
    private static final String MAPPER_PATH = "classpath:mapper/*.xml";
    
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {
        log.info("<== DataSource: {}", DataSource.class.getName());
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 设置 bean对象别名
//        bean.setTypeAliases(new Class<?>[] {UserDO.class});
        bean.setTypeAliasesPackage(BaseMarker.class.getPackageName());
        bean.setTypeAliasesSuperType(BaseDO.class);
        
        // 使用模式字符串路径加载sql mapper xml文件
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] mapperLocations = resolver.getResources(MAPPER_PATH);
        bean.setMapperLocations(mapperLocations);
        log.info("mapperLocations: {}", mapperLocations, null);
        
        // mybatis setting设置
        var config = new org.apache.ibatis.session.Configuration();
        config.setUseGeneratedKeys(true);
        config.setUseColumnLabel(true);
        config.setMapUnderscoreToCamelCase(true);
        
        bean.setConfiguration(config);
        log.info("==> SqlSessionFactoryBean: {}", bean);
        return bean;
    }
}
