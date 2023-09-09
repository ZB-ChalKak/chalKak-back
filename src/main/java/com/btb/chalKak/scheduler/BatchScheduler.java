package com.btb.chalKak.scheduler;

import java.time.LocalDateTime;
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

  private final Job tutorialJob;  // tutorialJob
  private final Job processJob;  // processJob
  private final JobLauncher jobLauncher;

  // 20초마다 실행
//  @Scheduled(fixedDelay = 2000 * 1000L)
//  public void executeJob () {
//    try {
//      jobLauncher.run(
//          job,
//          new JobParametersBuilder()
//              .addString("datetime", LocalDateTime.now().toString())
//              .toJobParameters()  // job parameter 설정
//      );
//    } catch (JobExecutionException ex) {
//      System.out.println(ex.getMessage());
//      ex.printStackTrace();
//    }
//  }

  @Scheduled(cron = "0 30 6 * * ?")
  public void runTutorialJob() {
    try {
      JobParameters params = new JobParametersBuilder()
              .addString("JobID", String.valueOf(System.currentTimeMillis()))
              .toJobParameters();
      jobLauncher.run(tutorialJob, params);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Scheduled(cron = "0 20 6 * * ?")
  public void runProcessJob() {
    try {
      JobParameters params = new JobParametersBuilder()
              .addString("JobID", String.valueOf(System.currentTimeMillis()))
              .toJobParameters();
      jobLauncher.run(processJob, params);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}