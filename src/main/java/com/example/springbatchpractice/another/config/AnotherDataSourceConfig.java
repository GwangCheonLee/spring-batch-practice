package com.example.springbatchpractice.another.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.springbatchpractice.another.repositories",
        entityManagerFactoryRef = "anotherEntityManagerFactory",
        transactionManagerRef = "anotherTransactionManager"
)
public class AnotherDataSourceConfig {

    @Bean(name = "anotherDataSource")
    public DataSource anotherDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/postgres")
                .username("postgres")
                .password("postgrespw")
                .build();
    }

    @Bean(name = "anotherEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean anotherEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("anotherDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.springbatchpractice.another.entities")
                .persistenceUnit("another")
                .build();
    }

    @Bean(name = "anotherTransactionManager")
    public PlatformTransactionManager anotherTransactionManager(@Qualifier("anotherEntityManagerFactory") EntityManagerFactory anotherEntityManagerFactory) {
        return new JpaTransactionManager(anotherEntityManagerFactory);
    }
}
