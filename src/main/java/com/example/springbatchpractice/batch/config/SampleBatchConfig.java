package com.example.springbatchpractice.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class SampleBatchConfig {

    @Bean("sampleJob")
    public Job configureSampleJob(JobRepository jobRepository, Step sampleStep) {
        return new JobBuilder("sampleJob", jobRepository)
                .start(sampleStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step sampleStep(JobRepository jobRepository, Tasklet sampleTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("sampleStep", jobRepository)
                .tasklet(sampleTasklet, platformTransactionManager)
                .build();
    }

}
