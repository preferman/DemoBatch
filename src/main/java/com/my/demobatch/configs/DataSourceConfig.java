package com.my.demobatch.configs;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 *  <a href="https://docs.spring.io/spring-boot/docs/2.7.14/reference/html/howto.html#howto.data-access.configure-two-datasources">...</a>
 *
 * @author job
 * @since 000
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(HikariDataSourceProperties.class)
public class DataSourceConfig {


    /**
     * <a href="https://docs.spring.io/spring-batch/docs/4.3.8/reference/html/job.html#inMemoryRepository">...</a>
     * @return datasource
     */
    @Bean
    @BatchDataSource
    public DataSource batchDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("/org/springframework/batch/core/schema-drop-hsqldb.sql")
                .addScript("/org/springframework/batch/core/schema-hsqldb.sql")
                .build();
    }


    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.app1")
    public DataSourceProperties app1DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public HikariDataSource app1DataSource(@Qualifier("app1DataSourceProperties") DataSourceProperties app1DataSourceProperties,
                                           HikariDataSourceProperties properties) {
        HikariDataSource hikariDataSource = app1DataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        hikariDataSource.setAutoCommit(properties.isAutoCommit());
        hikariDataSource.setConnectionTimeout(properties.getConnectionTimeout());
        hikariDataSource.setValidationTimeout(properties.getValidationTimeout());
        hikariDataSource.setMaxLifetime(properties.getMaxLifetime());
        hikariDataSource.setMaximumPoolSize(properties.getMaximumPoolSize());
        hikariDataSource.setMinimumIdle(properties.getMinimumIdle());
        return hikariDataSource;
    }

    @Bean
    public NamedParameterJdbcOperations namedParameterJdbcOperations(@Qualifier("app1DataSource") DataSource app1DataSource) {
        return new NamedParameterJdbcTemplate(app1DataSource);
    }





}
