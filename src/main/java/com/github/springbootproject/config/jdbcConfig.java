package com.github.springbootproject.config;

import com.github.springbootproject.properties.DataSourceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@RequiredArgsConstructor
public class jdbcConfig {

    private final DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource dataSource1() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        // mysql 드라이버 등록
        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        // database 주소
        dataSource.setUrl(dataSourceProperties.getUrl1());
        return dataSource;
    }

    @Bean
    public DataSource dataSource2() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        // mysql 드라이버 등록
        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        // database 주소
        dataSource.setUrl(dataSourceProperties.getUrl2());
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate1() {
        // jdbc template 등록
        return new JdbcTemplate(dataSource1());
    }

    @Bean
    public JdbcTemplate jdbcTemplate2() {
        // jdbc template 등록
        return new JdbcTemplate(dataSource2());
    }

    @Bean(name="tm1")
    public PlatformTransactionManager transactionManager1() {
        // transaction 사용 등록
        return new DataSourceTransactionManager(dataSource1());
    }

    @Bean(name="tm2")
    public PlatformTransactionManager transactionManager2() {
        // transaction 사용 등록
        return new DataSourceTransactionManager(dataSource2());
    }
}
