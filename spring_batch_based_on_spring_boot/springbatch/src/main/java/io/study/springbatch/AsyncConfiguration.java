package io.study.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;


@Configuration
@RequiredArgsConstructor
public class AsyncConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() throws InterruptedException {
        return jobBuilderFactory.get("job")
//                .start(syncStep())
                .start(asyncStep())
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Step syncStep() {
        return stepBuilderFactory.get("syncStep1")
                .<Customer, Customer>chunk(3)
                .reader(itemReader())
                .processor(customItemProcessor())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public Step asyncStep() throws InterruptedException {
        return stepBuilderFactory.get("asyncStep1")
                .<Customer, Customer>chunk(10)
                .reader(itemReader())
                .processor(asyncItemProcessor())
                .writer(asyncItemWriter())
                .build();
    }

    @Bean
    public ItemProcessor asyncItemProcessor() throws InterruptedException {
        AsyncItemProcessor<Customer, Customer> asyncItemProcessor = new AsyncItemProcessor();
        asyncItemProcessor.setDelegate(customItemProcessor());
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());

        return  asyncItemProcessor;
    }

    @Bean
    public ItemProcessor<Customer, Customer> customItemProcessor() {
        return new ItemProcessor<Customer, Customer>() {
            @Override
            public Customer process(Customer item) throws Exception {
                Thread.sleep(100);

                item.setName(item.getName().toUpperCase());
                return item;
            }
        };
    }

    @Bean
    public ItemWriter asyncItemWriter() {
        AsyncItemWriter<Customer> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(customItemWriter());

        return asyncItemWriter;
    }

    @Bean
    public ItemWriter<Customer> customItemWriter() {
        return new FlatFileItemWriterBuilder()
                .name("itemWriter1")
                .resource(new FileSystemResource("src/main/resources/converted_customer.csv"))
                .delimited().delimiter("|")
                .names(new String[]{"id", "name", "age"})
                .build();
    }

    @Bean
    public ItemReader itemReader() {
        return new FlatFileItemReaderBuilder()
                .name("itemReader1")
                .resource(new ClassPathResource("/customer.csv"))
                .delimited().delimiter(",")
                .names(new String[]{"id", "name", "age"})
                .linesToSkip(1)
                .targetType(Customer.class)
                .build();
    }
}
