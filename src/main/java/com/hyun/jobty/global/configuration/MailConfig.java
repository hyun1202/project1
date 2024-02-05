package com.hyun.jobty.global.configuration;

import com.hyun.jobty.global.configuration.property.GlobalProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@RequiredArgsConstructor
@Configuration
public class MailConfig {
    private final GlobalProperty.Mail mailProperty;
    @Value(value = "${spring.mail.host}")
    private String host;
    @Value(value = "${spring.mail.port}")
    private int port;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    public boolean auth;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    public boolean starttlsEnable;
    @Value("${spring.mail.properties.mail.debug}")
    public boolean isDebug;
    @Value(value = "${jobty.host}")
    public String appHost;
    @Bean
    public JavaMailSender javaMailService(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(mailProperty.getUsername());
        javaMailSender.setPassword(mailProperty.getPassword());
        javaMailSender.setJavaMailProperties(getMailProperties());
        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp"); // 프로토콜 설정
        properties.setProperty("mail.smtp.auth", String.valueOf(auth)); // smtp 인증
        properties.setProperty("mail.smtp.starttls.enable", String.valueOf(starttlsEnable)); // smtp strattles 사용
        properties.setProperty("mail.debug", String.valueOf(isDebug)); // 디버그 사용
        return properties;
    }

    public String getSenderEmail() {
        return mailProperty.getUsername();
    }

    public String getAppHost() {
        return appHost;
    }
}
