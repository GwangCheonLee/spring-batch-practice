package com.example.springbatchpractice.quartz.config;

import com.example.springbatchpractice.quartz.job.QuartzAnotherJob;
import com.example.springbatchpractice.quartz.job.QuartzSampleJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public Trigger quartzJobSampleTrigger(JobDetail quartzSampleJobDetail) {
        String everyFiveSecondsCron = "0/5 * * * * ?";

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();

        triggerBuilder.forJob(quartzSampleJobDetail);
        triggerBuilder.withIdentity("quartzSampleJobTrigger");
        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(everyFiveSecondsCron));

        return triggerBuilder.build();
    }

    @Bean
    public JobDetail quartzSampleJobDetail() {
        JobBuilder jobBuilder = JobBuilder.newJob(QuartzSampleJob.class);

        jobBuilder.withIdentity("quartzSampleJobDetail");
        jobBuilder.storeDurably();

        return jobBuilder.build();
    }

    @Bean
    public Trigger quartzAnotherJobTrigger(JobDetail quartzAnotherJobDetail) {
        String everyFiveSecondsCron = "0/10 * * * * ?";

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();

        triggerBuilder.forJob(quartzAnotherJobDetail);
        triggerBuilder.withIdentity("quartzAnotherJobTrigger");
        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(everyFiveSecondsCron));

        return triggerBuilder.build();
    }

    @Bean
    public JobDetail quartzAnotherJobDetail() {
        JobBuilder jobBuilder = JobBuilder.newJob(QuartzAnotherJob.class);

        jobBuilder.withIdentity("quartzAnotherJobDetail");
        jobBuilder.storeDurably();

        return jobBuilder.build();
    }

}
