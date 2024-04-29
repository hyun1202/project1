package com.jobty.conf;

import com.jobty.conf.property.GlobalProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
public class DatabaseConfig {
    private final GlobalProperty.Database databaseProperty;

    @Bean
    public DataSource customDataSource(){
        return DataSourceBuilder
                .create()
                .url(databaseProperty.getUrl())
                .username(databaseProperty.getUsername())
                .password(databaseProperty.getPassword())
                .build();
    }
}
