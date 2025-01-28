package com.example.springbatchpractice.batch.job;

import static com.example.springbatchpractice.another.entity.QAnother.another;

import com.example.springbatchpractice.another.entity.Another;
import com.example.springbatchpractice.batch.itemreader.QuerydslNoOffsetPagingItemReader;
import com.example.springbatchpractice.batch.itemreader.expression.Expression;
import com.example.springbatchpractice.batch.itemreader.options.QuerydslNoOffsetNumberOptions;
import com.example.springbatchpractice.common.property.BatchProperties;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * Created by jojoldu@gmail.com on 06/10/2019 Blog : http://jojoldu.tistory.com Github :
 * http://github.com/jojoldu
 */

@Slf4j // log 사용을 위한 lombok 어노테이션
@Configuration
public class QuerydslNoOffsetPagingItemReaderConfiguration {

    public static final String JOB_NAME = "querydslNoOffsetPagingReaderJob";
    public static final String STEP_NAME = "querydslNoOffsetPagingReaderStep";
    private final int chunkSize;

    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final PlatformTransactionManager transactionManager;


    public QuerydslNoOffsetPagingItemReaderConfiguration(
        JobRepository jobRepository,
        @Qualifier("anotherEntityManagerFactory") EntityManagerFactory entityManagerFactory,
        @Qualifier("anotherTransactionManager") PlatformTransactionManager transactionManager,
        BatchProperties properties) {
        this.jobRepository = jobRepository;
        this.entityManagerFactory = entityManagerFactory;
        this.transactionManager = transactionManager;
        this.chunkSize = properties.getChunkSize();
    }

    @Bean
    public Job querydslNoOffsetPagingItemReaderJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
            .start(querydslNoOffsetPagingItemReaderStep())
            .build();
    }

    @Bean
    public Step querydslNoOffsetPagingItemReaderStep() {
        return new StepBuilder(STEP_NAME, jobRepository)
            .<Another, Another>chunk(chunkSize, transactionManager)
            .reader(querydslNoOffsetPagingItemReaderReader())
            .processor(querydslNoOffsetPagingItemReaderProcessor())
            .writer(querydslNoOffsetPagingItemReaderWriter())
            .build();
    }

    @Bean
    public QuerydslNoOffsetPagingItemReader<Another> querydslNoOffsetPagingItemReaderReader() {
        // 1. No Offset 옵션
        QuerydslNoOffsetNumberOptions<Another, Long> options =
            new QuerydslNoOffsetNumberOptions<>(another.id, Expression.ASC);

        // 2. Querydsl
        return new QuerydslNoOffsetPagingItemReader<>(entityManagerFactory, chunkSize, options,
            queryFactory -> queryFactory
                .selectFrom(another));
    }

    private ItemProcessor<Another, Another> querydslNoOffsetPagingItemReaderProcessor() {
        String executionTime = java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return item -> {
            item.setColumn1(executionTime + " - " + "column1");
            item.setColumn2(executionTime + " - " + "column2");
            return item;
        };
    }

    @Bean
    public JpaItemWriter<Another> querydslNoOffsetPagingItemReaderWriter() {
        return new JpaItemWriterBuilder<Another>()
            .entityManagerFactory(entityManagerFactory)
            .build();
    }
}
