package com.my.demobatch.readers;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
public class MyReader implements ItemReader<Void> {
    @Override
    public Void read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        System.err.println("reading");
        return null;
    }
}
