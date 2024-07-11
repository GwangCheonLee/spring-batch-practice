package com.example.springbatchpractice.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class QuartzJob implements Job {

    // Spring Batch 작업을 실행하기 위한 JobLauncher
    private final JobLauncher jobLauncher;
    // 실행할 Spring Batch 작업
    private final org.springframework.batch.core.Job batchJob;

    // 생성자를 통한 의존성 주입
    public QuartzJob(JobLauncher jobLauncher, @Qualifier("sampleJob") org.springframework.batch.core.Job batchJob) {
        this.jobLauncher = jobLauncher;
        this.batchJob = batchJob;
    }

    // Quartz 스케줄러에 의해 실행될 메소드
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            // 고유성을 보장하기 위해 현재 시간을 기반으로 한 파라미터와 함께 작업 실행
            jobLauncher.run(batchJob, new JobParametersBuilder().addLong("uniqueness", System.nanoTime()).toJobParameters());
        } catch (Exception e) {
            // 실행 중 예외 발생 시 JobExecutionException으로 포장하여 Quartz에 전달
            throw new JobExecutionException(e);
        }
    }
}
