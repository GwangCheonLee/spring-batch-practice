package com.example.springbatchpractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringBatchPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchPracticeApplication.class, args);
    }
}
