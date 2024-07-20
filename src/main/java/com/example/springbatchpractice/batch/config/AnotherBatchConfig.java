package com.example.springbatchpractice.batch.config;

import com.example.springbatchpractice.another.entities.Another;
import com.example.springbatchpractice.another.repositories.AnotherRepository;
import com.example.springbatchpractice.batch.processor.AnotherItemProcessor;
import com.example.springbatchpractice.batch.writer.AnotherItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
public class AnotherBatchConfig {

    private final int CHUNK_SIZE = 10;
    private final AnotherItemProcessor anotherItemProcessor;
    private final AnotherItemWriter anotherItemWriter;
    private final AnotherRepository anotherRepository;

    public AnotherBatchConfig(AnotherItemProcessor anotherItemProcessor, AnotherItemWriter anotherItemWriter, AnotherRepository anotherRepository) {
        this.anotherItemProcessor = anotherItemProcessor;
        this.anotherItemWriter = anotherItemWriter;
        this.anotherRepository = anotherRepository;
    }

    @Bean("anotherJob")
    public Job anotherJob(JobRepository jobRepository, Step anotherStep) {
        return new JobBuilder("anotherJob", jobRepository)
                .start(anotherStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step anotherStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("anotherStep", jobRepository)
                .<Another, Another>chunk(CHUNK_SIZE, transactionManager)
                .reader(anotherItemReader())
                .processor(anotherItemProcessor)
                .writer(anotherItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Another> anotherItemReader() {
        return new RepositoryItemReaderBuilder<Another>()
                .name("anotherItemReader")
                .repository(anotherRepository)
                .methodName("findBy")
                .pageSize(CHUNK_SIZE)
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }
}
