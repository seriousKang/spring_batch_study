package io.study.springbatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.batch.repeat.RepeatStatus.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobParameterConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                        JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
//                        String name = jobParameters.getString("name");
//                        Date date = jobParameters.getDate("date");
//                        Long seq = jobParameters.getLong("seq");
//                        Double score = jobParameters.getDouble("score");
//                        log.info("name  = " + name);
//                        log.info("date  = " + date);
//                        log.info("seq   = " + seq);
//                        log.info("score = " + score);

                        Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
                        log.info("jobParameters = [{}]", jobParameters);

                        return FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("step2 start!");
                    return FINISHED;
                }))
                .build();
    }
}
