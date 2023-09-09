package com.btb.chalKak.batch;

import com.btb.chalKak.batch.listener.TableClearingListener;
import com.btb.chalKak.batch.processor.PostItemProcessor;
import com.btb.chalKak.batch.reader.PostItemReader;
import com.btb.chalKak.batch.writer.RecommendPostItemWriter;
import com.btb.chalKak.domain.batchpost.entity.RecommendPostBatch;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.weather.entity.Weather;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final RecommendPostItemWriter recommendPostItemWriter;
  private final TableClearingListener tableClearingListener;

  private final PostItemReader postItemReader;

  private final PostItemProcessor postItemProcessor;

  private static final int chunkSize = 10;

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

  @Bean
  public Job processJob() {
    return jobBuilderFactory.get("processJob")
            .listener(tableClearingListener)
            .flow(orderStep1())
            .end()
            .build();
  }

  @Bean
  public Step orderStep1() {
    return stepBuilderFactory.get("orderStep1")
            .<Post, RecommendPostBatch>chunk(chunkSize)
            .reader(postItemReader)
            .processor(postItemProcessor)
            .writer(recommendPostItemWriter)
            .build();
  }

}
