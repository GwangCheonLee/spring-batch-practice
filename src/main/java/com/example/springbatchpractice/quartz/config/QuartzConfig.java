package com.example.springbatchpractice.quartz.config;

import com.example.springbatchpractice.quartz.job.QuartzJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public Trigger quartzJobTrigger(JobDetail quartzJobDetail) {
        String everyFiveSecondsCron = "0/5 * * * * ?";

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();

        triggerBuilder.forJob(quartzJobDetail);
        triggerBuilder.withIdentity("quartzJobTrigger");
        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(everyFiveSecondsCron));

        return triggerBuilder.build();
    }

    @Bean
    public JobDetail quartzJobDetail() {
        JobBuilder jobBuilder = JobBuilder.newJob(QuartzJob.class);

        jobBuilder.withIdentity("quartzJobDetail");
        jobBuilder.storeDurably();

        return jobBuilder.build();
    }

}
