package com.example.springbatchpractice.batch.processor;

import com.example.springbatchpractice.another.entities.Another;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnotherItemProcessor implements ItemProcessor<Another, Another> {

    @Override
    public Another process(Another another) {
        log.info("Processing another: {}", another);
        another.setEmail(another.getEmail() + "@example.com");
        return another;
    }
}
