package com.example.springbatchpractice.batch.writer;

import com.example.springbatchpractice.another.entities.Another;
import com.example.springbatchpractice.another.repositories.AnotherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnotherItemWriter implements ItemWriter<Another> {

    private final AnotherRepository anotherRepository;

    public AnotherItemWriter(AnotherRepository anotherRepository) {
        this.anotherRepository = anotherRepository;

    }

    @Override
    public void write(Chunk<? extends Another> chunk) {
        for (Another item : chunk.getItems()) {
            log.info("Writing item: {}", item);
            anotherRepository.save(item);
        }
    }
}
