package com.example.springbatchpractice.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    // 매 5초마다 작업을 실행하는 CRON 표현식을 상수로 정의합니다.
    private static final String EVERY_FIVE_SECONDS_CRON = "0/5 * * * * ?";
    private static final Logger log = LoggerFactory.getLogger(QuartzConfig.class);

    @Bean
    public Trigger quartzJobTrigger(JobDetail quartzJobDetail) {
        log.info("quartzJobDetail = {}", quartzJobDetail);
        // quartzJobDetail에 대한 Trigger를 생성합니다. 이 트리거는 작업이 언제 실행될지를 결정합니다.
        return TriggerBuilder.newTrigger()
                .forJob(quartzJobDetail) // 실행할 JobDetail을 지정합니다.
                .withIdentity("quartzJobTrigger") // 트리거의 고유 식별자를 설정합니다.
                .withSchedule(CronScheduleBuilder.cronSchedule(EVERY_FIVE_SECONDS_CRON)) // 트리거의 스케줄을 설정합니다.
                .build();
    }

    @Bean
    public JobDetail quartzJobDetail() {
        // QuartzJob을 실행하기 위한 JobDetail을 생성합니다.
        return JobBuilder.newJob(QuartzJob.class)
                .withIdentity("quartzJobDetail") // 작업의 고유 식별자를 설정합니다.
                .storeDurably() // 작업이 트리거 없이도 저장되어야 함을 나타냅니다.
                .build();
    }

}
