package cn.navyd.app.supermarket.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Data;

@EnableConfigurationProperties
@Configuration
public class DatasourceConfig {

    @Profile(SupermarketProfiles.PRODUCTION)
    @Bean
    public DataSource hikariDataSource(MysqlDatasourceProperties mysqlProperties, HikariProperties hkProperties) throws SQLException {
        MysqlDataSource mysqlDataSource = getMysqlDataSource(mysqlProperties);
        HikariDataSource bean = new HikariDataSource();
        bean.setDataSource(mysqlDataSource);
        bean.setMaximumPoolSize(hkProperties.getMaximumPoolSize());
        bean.setMinimumIdle(hkProperties.getMinimumIdle());
        bean.setAutoCommit(hkProperties.getAutoCommit());
        bean.setConnectionTimeout(hkProperties.getConnectionTimeout());
        return bean;
    }
    
    @Profile(SupermarketProfiles.DEVELOPMENT)
    @Bean
    public DataSource embeddedDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("test")
                .addScript("classpath:./db/h2/schema.sql")
                .addScript("classpath:./db/h2/test-data.sql")
                .build();
    }
    
    
    private MysqlDataSource getMysqlDataSource(MysqlDatasourceProperties properties) throws SQLException {
        MysqlDataSource bean = new MysqlDataSource();
        bean.setUrl(properties.getUrl());
        bean.setUser(properties.getUsername());
        bean.setPassword(properties.getPassword());
        bean.setCharacterEncoding(properties.getCharacterEncoding());
        bean.setUseSSL(properties.getUseSsl());
        bean.setCachePrepStmts(properties.getCachePrepStmts());
        bean.setPrepStmtCacheSize(properties.getPrepStmtCacheSize());
        bean.setPrepStmtCacheSqlLimit(properties.getPrepStmtCacheSqlLimit());
        bean.setUseServerPrepStmts(properties.getUseServerPrepStmts());
        bean.setUseLocalSessionState(properties.getUseLocalSessionState());
        bean.setRewriteBatchedStatements(properties.getRewriteBatchedStatements());
        bean.setCacheResultSetMetadata(properties.getCacheResultSetMetadata());
        bean.setCacheServerConfiguration(properties.getCacheResultSetMetadata());
        bean.setElideSetAutoCommits(properties.getElideSetAutoCommits());
        bean.setMaintainTimeStats(properties.getMaintainTimeStats());
        bean.setServerTimezone(properties.getServerTimezone());
        return bean;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public class HikariProperties {
        private Boolean autoCommit;
        private Integer maximumPoolSize;
        private Integer minimumIdle;
        private Long connectionTimeout;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix="mysql.datasource")
    public class MysqlDatasourceProperties {
        private Boolean useSsl;
        private String serverTimezone;
        private String url;
        private String username;
        private String password;
        private String characterEncoding;
        private Boolean cachePrepStmts;
        private Integer prepStmtCacheSize;
        private Integer prepStmtCacheSqlLimit;
        private Boolean useServerPrepStmts;
        private Boolean useLocalSessionState;
        private Boolean rewriteBatchedStatements;
        private Boolean cacheResultSetMetadata;
        private Boolean cacheServerConfiguration;
        private Boolean elideSetAutoCommits;
        private Boolean maintainTimeStats;
    }

}

