package com.btb.chalKak.scheduler;

import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchScheduler {

  private final Job jpaItemWriterJob;
  private final JobLauncher jobLauncher;

  @PostConstruct
  public void init() {
//    jpaItemWriterJob();
  }
  @Scheduled(cron = "0 20 6 * * ?")
  public void jpaItemWriterJob() {
    try {
      JobParameters params = new JobParametersBuilder()
              .addString("JobID", String.valueOf(System.currentTimeMillis()))
              .toJobParameters();
      jobLauncher.run(jpaItemWriterJob, params);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}