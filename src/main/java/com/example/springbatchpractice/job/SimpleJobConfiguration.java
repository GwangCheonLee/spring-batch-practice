package com.example.springbatchpractice.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class SimpleJobConfiguration {

    // sampleJob을 정의하는 메소드입니다. 이 메소드는 Spring Batch 작업을 구성합니다.
    @Bean("sampleJob")
    public Job configureSampleJob(JobRepository jobRepository, Step sampleStep) {
        log.info(">>>>> sampleJob 정의");
        return new JobBuilder("sampleJob", jobRepository)
                .start(sampleStep) // sampleStep을 시작 단계로 설정합니다.
                .build();

    }

    // sampleStep을 정의하는 메소드입니다. 이 메소드는 작업의 단계를 구성합니다.
    @Bean("sampleStep")
    public Step configureSampleStep(JobRepository jobRepository, Tasklet sampleTasklet, PlatformTransactionManager platformTransactionManager) {
        log.info(">>>>> sampleStep 정의");
        return new StepBuilder("sampleStep", jobRepository)
                .tasklet(sampleTasklet, platformTransactionManager) // sampleTasklet을 단계의 태스클릿으로 설정합니다.
                .build();
    }

    // sampleTasklet을 정의하는 메소드입니다. 이 태스클릿은 실제 작업을 수행합니다.
    @Bean("sampleTasklet")
    public Tasklet configureSampleTasklet() {
        log.info(">>>>> sampleTasklet 정의");
        return (contribution, chunkContext) -> {
            log.info(">>>>> sampleTasklet 실행");
            return RepeatStatus.FINISHED; // 작업이 완료되었음을 나타냅니다.
        };
    }
}
