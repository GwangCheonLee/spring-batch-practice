package com.example.springbatchpractice.batch.tasklet;

import com.example.springbatchpractice.another.entities.Another;
import com.example.springbatchpractice.another.repositories.AnotherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SampleTasklet implements Tasklet {

    private final AnotherRepository anotherRepository;

    public SampleTasklet(AnotherRepository anotherRepository) {
        this.anotherRepository = anotherRepository;
    }

    @Override
    public RepeatStatus execute(org.springframework.batch.core.StepContribution contribution, org.springframework.batch.core.scope.context.ChunkContext chunkContext) throws Exception {
        log.info("Run SampleTasklet");

        List<Another> anotherList = anotherRepository.findAllAnother();
        log.info("Select all from another table");

        for (Another another : anotherList) {
            log.info(another.toString());
            log.info("Print all data from another table");
        }

        log.info("Wait for 5 seconds");
        Thread.sleep(5000);
        log.info("End SampleTasklet");

        return RepeatStatus.FINISHED;
    }
}
