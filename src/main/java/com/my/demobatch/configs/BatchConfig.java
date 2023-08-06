package com.my.demobatch.configs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * <a href="https://docs.spring.io/spring-batch/docs/4.3.8/reference/html/job.html#configureJob">...</a>
 */
@Configuration(proxyBeanMethods = false)
@EnableBatchProcessing
public class BatchConfig {


    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;


    @Bean
    public BatchConfigurer batchConfigurer(@Qualifier("batchDataSource") DataSource dataSource) {
        return new DefaultBatchConfigurer(dataSource) {
            @Override
            public PlatformTransactionManager getTransactionManager() {
                return new DataSourceTransactionManager(dataSource);
            }
        };
    }


    @Bean
    public Job job(@Qualifier("step1") Step step1, @Qualifier("step2") Step step2) {
        return jobs.get("myJob").start(step1).next(step2).build();
    }

    @Bean
    protected Step step1(ItemReader<Void> reader,
                         ItemProcessor<Void, Void> processor,
                         ItemWriter<Void> writer) {
        return steps.get("step1")
                .<Void, Void>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    protected Step step2() {
        return steps.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.err.println("step2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
