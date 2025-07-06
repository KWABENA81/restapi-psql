package com.asksef.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;

//@Configuration
public class DataSourceConfig {

    @Bean
    public HikariDataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();
        try {

            config.setDriverClassName("oracle.jdbc.OracleDriver");
            config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521/xepdb1");
            config.setConnectionTestQuery("SELECT * FROM DUAL");
            config.setUsername("db_user");
            config.setPassword("asksef");
            config.setMaximumPoolSize(4);
            config.addDataSourceProperty("oracle.jdbc.defaultConnectionValidation", "LOCAL");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new HikariDataSource(config);
    }
}
