package com.example.springbatchpractice.another.repositories;

import com.example.springbatchpractice.another.entities.Another;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.springbatchpractice.another.entities.QAnother.another;

@Repository
public class AnotherRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public AnotherRepository(@Qualifier("anotherJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Another> findAllAnother() {
        return jpaQueryFactory.select(Projections.bean(Another.class,
                        another.id,
                        another.name,
                        another.email
                ))
                .from(another)
                .fetch();
    }
}
