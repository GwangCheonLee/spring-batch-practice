package com.example.springbatchpractice.batch.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.springbatchpractice.batch",
        entityManagerFactoryRef = "batchEntityManagerFactory",
        transactionManagerRef = "batchTransactionManager"
)
public class BatchDataSourceConfig {

    @Primary
    @Bean(name = "batchDataSource")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/batch")
                .username("postgres")
                .password("postgrespw")
                .build();
    }

    @Primary
    @Bean(name = "batchEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean batchEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("batchDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.springbatchpractice.batch.entities")
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
