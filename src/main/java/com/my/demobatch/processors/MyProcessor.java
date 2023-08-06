package com.my.demobatch.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class MyProcessor implements ItemProcessor<Void, Void> {

    @Override
    public Void process(Void item) throws Exception {
        System.out.println("process");
        return null;
    }
}
