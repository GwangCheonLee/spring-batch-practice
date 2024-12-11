package com.example.springbatchpractice.common.config;

import com.example.springbatchpractice.common.property.AnotherDataSourceProperties;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.example.springbatchpractice.another.repository",
    entityManagerFactoryRef = "anotherEntityManagerFactory",
    transactionManagerRef = "anotherTransactionManager"
)
@RequiredArgsConstructor
public class AnotherDataSourceConfig {

    private final AnotherDataSourceProperties properties;

    @Bean(name = "anotherDataSource")
    public DataSource anotherDataSource() {
        return DataSourceBuilder.create()
            .driverClassName(properties.getDriverClassName())
            .url(properties.getUrl())
            .username(properties.getUsername())
            .password(properties.getPassword())
            .build();
    }

    @Bean(name = "anotherEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean anotherEntityManagerFactory(
        EntityManagerFactoryBuilder builder,
        @Qualifier("anotherDataSource") DataSource dataSource) {
        return builder
            .dataSource(dataSource)
            .packages("com.example.springbatchpractice.another.entity")
            .persistenceUnit("another")
            .build();
    }

    @Bean(name = "anotherTransactionManager")
    public PlatformTransactionManager anotherTransactionManager(
        @Qualifier("anotherEntityManagerFactory") EntityManagerFactory anotherEntityManagerFactory) {
        return new JpaTransactionManager(anotherEntityManagerFactory);
    }
}
