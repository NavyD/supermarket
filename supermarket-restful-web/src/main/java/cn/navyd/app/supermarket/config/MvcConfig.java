package cn.navyd.app.supermarket.config;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.navyd.app.supermarket.base.NotFoundException;
import cn.navyd.app.supermarket.util.ResponseResult;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 异常处理
     * @author navyd
     *
     */
    @RestControllerAdvice
    public static class MvcExceptionHandler {

        @ExceptionHandler(NotFoundException.class)
        @ResponseStatus(NOT_FOUND)
        public ResponseResult<Void> handleNotFoundException(NotFoundException ex) {
            return ResponseResult.ofError(getReadableMessage(ex));
        }

        /**
         * 控制controller没有处理的service抛出的illegalArgumentexception
         * 
         * @param e
         * @return
         */
        @ResponseStatus(UNPROCESSABLE_ENTITY)
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseResult<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
            return ResponseResult.ofError(getReadableMessage(ex));
        }

        private String getReadableMessage(Exception ex) {
            String exName = ex.getClass().getSimpleName();
            return exName + ": " + ex.getMessage();
        }
    }
    
    /**
     * https配置
     * @author navyd
     *
     */
    @Configuration
    static class ConnectorConfig {
        // 配置tomcat http重定向到https
        private Connector getHttpConnector() {
            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setScheme("http");
            connector.setPort(8080);
            connector.setSecure(false);
            connector.setRedirectPort(8443);
            return connector;
        }
        
        @Bean
        public ServletWebServerFactory servletContainer() {
            TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
                @Override
                protected void postProcessContext(Context context) {
                    SecurityConstraint constraint = new SecurityConstraint();
                    constraint.setUserConstraint("CONFIDENTIAL");
                    SecurityCollection collection = new SecurityCollection();
                    collection.addPattern("/*");
                    constraint.addCollection(collection);
                    context.addConstraint(constraint);
                }
            };
            tomcat.addAdditionalTomcatConnectors(getHttpConnector());
            return tomcat;
        }
    }
}


