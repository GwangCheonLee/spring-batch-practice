package com.example.springbatchpractice.common.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.example.springbatchpractice.batch",
    entityManagerFactoryRef = "batchEntityManagerFactory",
    transactionManagerRef = "batchTransactionManager"
)
public class BatchDataSourceConfig {

    @Value("${spring.datasource.batch.url}")
    private String batchDbUrl;

    @Value("${spring.datasource.batch.username}")
    private String batchDbUsername;

    @Value("${spring.datasource.batch.password}")
    private String batchDbPassword;

    @Value("${spring.datasource.batch.driver-class-name}")
    private String batchDbDriverClassName;

    @Primary
    @Bean(name = "batchDataSource")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create()
            .driverClassName(batchDbDriverClassName)
            .url(batchDbUrl)
            .username(batchDbUsername)
            .password(batchDbPassword)
            .build();
    }

    @Primary
    @Bean(name = "batchEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean batchEntityManagerFactory(
        EntityManagerFactoryBuilder builder,
        @Qualifier("batchDataSource") DataSource dataSource) {
        return builder
            .dataSource(dataSource)
            .packages("com.example.springbatchpractice.batch")
            .persistenceUnit("batch")
            .build();
    }

    @Primary
    @Bean(name = "batchTransactionManager")
    public PlatformTransactionManager batchTransactionManager(
        @Qualifier("batchEntityManagerFactory") EntityManagerFactory batchEntityManagerFactory) {
        return new JpaTransactionManager(batchEntityManagerFactory);
    }
}
