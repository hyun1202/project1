package com.hyun.jobty.global.configuration.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "file:${CONF_DIR}/conf.properties")
public class GlobalProperty {

    @Configuration
    public static class Database{
        @Value(value = "${spring.datasource.url}")
        private String url;
        @Value(value = "${spring.datasource.username}")
        private String username;
        @Value(value = "${spring.datasource.password}")
        private String password;

        public String getUrl() {
            return url;
        }

        public String getUsername() {
            return username;
        }
        public String getPassword() {
            return password;
        }
    }

    @Configuration
    public static class Redis{
        @Value(value = "${spring.data.redis.host}")
        private String host;
        @Value(value = "${spring.data.redis.port}")
        private int port;
//        @Value(value = "${spring.data.redis.password}")
//        private String password;

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

//        public String getPassword() {
//            return password;
//        }
    }

    @Configuration
    public static class Mail{
        @Value("${spring.mail.username}")
        private String username;
        @Value("${spring.mail.password}")
        private String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

}
