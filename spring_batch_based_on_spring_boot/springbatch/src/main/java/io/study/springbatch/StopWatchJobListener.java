package io.study.springbatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class StopWatchJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long elapsedTime = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();
        log.info("elapsedTime = {}", elapsedTime);
    }
}
