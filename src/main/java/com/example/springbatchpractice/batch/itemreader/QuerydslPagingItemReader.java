package com.example.springbatchpractice.batch.itemreader;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import lombok.Setter;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.util.ClassUtils;

/**
 * Querydsl을 기반으로 한 Spring Batch의 페이징 ItemReader 구현 클래스.
 *
 * @param <T> 읽어올 데이터의 타입
 */
public class QuerydslPagingItemReader<T> extends AbstractPagingItemReader<T> {

    /**
     * EntityManagerFactory를 통해 EntityManager를 생성.
     */
    private final EntityManagerFactory entityManagerFactory;

    /**
     * Querydsl JPAQuery를 생성하는 함수.
     */
    private final Function<JPAQueryFactory, JPAQuery<T>> queryFunction;

    /**
     * 데이터베이스와의 작업을 수행하는 JPA EntityManager.
     */
    private EntityManager entityManager;

    /**
     * 트랜잭션 활성화 여부 설정 (기본값: false).
     */
    @Setter
    private boolean transacted = false;

    /**
     * QuerydslPagingItemReader의 생성자.
     *
     * @param entityManagerFactory EntityManagerFactory 객체
     * @param pageSize             페이징 단위 크기
     * @param queryFunction        Querydsl 쿼리를 생성하는 함수
     */
    public QuerydslPagingItemReader(EntityManagerFactory entityManagerFactory,
        int pageSize,
        Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
        this.entityManagerFactory = entityManagerFactory;
        this.queryFunction = queryFunction;
        setPageSize(pageSize);
        setName(ClassUtils.getShortName(QuerydslPagingItemReader.class));
    }

    /**
     * ItemReader를 열고 EntityManager를 초기화합니다.
     *
     * @throws Exception 예외 발생 시 상위 클래스에 전달
     */
    @Override
    protected void doOpen() throws Exception {
        super.doOpen();
        entityManager = entityManagerFactory.createEntityManager();
        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        }
    }

    /**
     * 현재 페이지의 데이터를 읽어옵니다.
     *
     * @throws IllegalStateException EntityManager가 초기화되지 않았을 경우
     */
    @Override
    protected void doReadPage() {
        if (entityManager == null) {
            throw new IllegalStateException("EntityManager is not initialized");
        }

        JPQLQuery<T> query = createQuery()
            .offset((long) getPage() * getPageSize())
            .limit(getPageSize());

        executeQuery(query);
    }

    /**
     * Querydsl 쿼리를 생성합니다.
     *
     * @return JPQLQuery Querydsl 쿼리 객체
     */
    private JPQLQuery<T> createQuery() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFunction.apply(queryFactory);
    }

    /**
     * 생성된 Querydsl 쿼리를 실행하여 결과를 가져옵니다.
     *
     * @param query 실행할 Querydsl 쿼리
     */
    private void executeQuery(JPQLQuery<T> query) {
        try {
            List<T> queryResults = query.fetch();
            results.clear();
            if (transacted) {
                // 트랜잭션이 활성화된 경우
                entityManager.getTransaction().begin();
                results.addAll(queryResults);
                entityManager.getTransaction().commit();
            } else {
                // 트랜잭션이 비활성화된 경우
                queryResults.forEach(entity -> {
                    entityManager.detach(entity); // 엔티티를 detach 처리
                    results.add(entity);
                });
            }
        } catch (Exception e) {
            if (transacted && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error executing Querydsl query", e);
        }
    }

    /**
     * ItemReader를 닫고 EntityManager를 종료합니다.
     *
     * @throws Exception 예외 발생 시 상위 클래스에 전달
     */
    @Override
    protected void doClose() throws Exception {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        super.doClose();
    }
}
