package com.btb.chalKak.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class exampleBatch {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job tutorialJob() {
    return jobBuilderFactory.get("tutorialJob")
        .start(tutorialStep())  // Step 설정
        .build();
  }

  @Bean
  public Step tutorialStep() {
    return stepBuilderFactory.get("tutorialStep")
        .tasklet(new TutorialTasklet()) // Tasklet 설정
        .build();
  }

}
