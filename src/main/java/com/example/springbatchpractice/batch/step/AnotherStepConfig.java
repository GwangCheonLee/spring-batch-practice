package com.example.springbatchpractice.batch.step;

import com.example.springbatchpractice.another.entities.Another;
import com.example.springbatchpractice.batch.processor.AnotherItemProcessor;
import com.example.springbatchpractice.batch.reader.AnotherItemReader;
import com.example.springbatchpractice.batch.writer.AnotherItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class AnotherStepConfig {

    private final AnotherItemReader anotherItemReader;
    private final AnotherItemProcessor anotherItemProcessor;
    private final AnotherItemWriter anotherItemWriter;

    public AnotherStepConfig(AnotherItemReader anotherItemReader, AnotherItemProcessor anotherItemProcessor, AnotherItemWriter anotherItemWriter) {
        this.anotherItemReader = anotherItemReader;
        this.anotherItemProcessor = anotherItemProcessor;
        this.anotherItemWriter = anotherItemWriter;
    }

    @Bean
    public Step anotherStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("anotherStep", jobRepository)
                .<Another, Another>chunk(10, transactionManager)
                .reader(anotherItemReader)
                .processor(anotherItemProcessor)
                .writer(anotherItemWriter)
                .build();
    }
}
