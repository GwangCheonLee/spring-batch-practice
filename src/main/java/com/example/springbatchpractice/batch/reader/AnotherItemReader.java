package com.example.springbatchpractice.batch.reader;

import com.example.springbatchpractice.another.entities.Another;
import com.example.springbatchpractice.another.repositories.AnotherRepositorySupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class AnotherItemReader implements ItemReader<Another> {

    private static final Logger log = LoggerFactory.getLogger(AnotherItemReader.class);
    private final AnotherRepositorySupport anotherRepositorySupport;
    private Iterator<Another> iterator;

    public AnotherItemReader(AnotherRepositorySupport anotherRepositorySupport) {
        this.anotherRepositorySupport = anotherRepositorySupport;
    }

    @Override
    public Another read() {
        if (iterator == null) {
            List<Another> anotherList = anotherRepositorySupport.findAllAnother();

            for (Another another : anotherList) {
                log.info("Reading item: {}", another);
            }

            iterator = anotherList.iterator();
        }

        return iterator.hasNext() ? iterator.next() : null;
    }
}
