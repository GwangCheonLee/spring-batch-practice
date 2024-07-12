package com.example.springbatchpractice.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {

    @PersistenceContext(unitName = "batch")
    private EntityManager batchEntityManager;

    @PersistenceContext(unitName = "another")
    private EntityManager anotherEntityManager;

    @Bean
    public JPAQueryFactory batchJpaQueryFactory() {
        return new JPAQueryFactory(batchEntityManager);
    }

    @Bean
    public JPAQueryFactory anotherJpaQueryFactory() {
        return new JPAQueryFactory(anotherEntityManager);
    }
}
