package com.star.bank.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "pg.database")
    public DataSource postgresDataSource() {
        return new HikariDataSource();
    }

    @Bean
    @ConfigurationProperties(prefix = "h2.database")
    public DataSource h2DataSource() {
        return new HikariDataSource();
    }

    @Bean
    public JdbcTemplate h2JdbcTemplate(@Qualifier("h2DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}