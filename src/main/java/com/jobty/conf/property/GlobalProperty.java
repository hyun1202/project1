package com.jobty.conf.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "file:${CONF_DIR}/conf.yaml", factory = YamlPropertySourceFactory.class)
public class GlobalProperty {

    @Configuration
//    @ConfigurationProperties(prefix = "spring.datasource")
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
    @Getter
    public static class Mail{
        @Value("${spring.mail.host}")
        private String host;
        @Value("${spring.mail.port}")
        private int port;
        @Value("${spring.mail.username}")
        private String username;
        @Value("${spring.mail.password}")
        private String password;
        @Value("${jobty.mail.link_host}")
        private String linkHost;
    }

    @Configuration
    public static class Jobty{
        @Getter
        @Configuration
        public static class Key{
            @Value("${jobty.key.aes.normal_key}")
            private String normal_key;
            @Value("${jobty.key.aes.admin_key}")
            private String admin_key;
            @Value("${jobty.key.aes.super_key}")
            private String super_key;
            @Value("${jobty.key.aes.id_key}")
            private String id_key;
        }
        @Getter
        @Configuration
        public static class File{
            @Value("${jobty.file.upload_path}")
            private String upload_path;
        }
    }
}
