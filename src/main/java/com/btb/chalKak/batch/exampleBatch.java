package com.btb.chalKak.batch;

import com.btb.chalKak.domain.weather.entity.Weather;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class exampleBatch {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

//  @Bean
//  public Job sampleDb2FileChunkJob(SampleJobListener jobListener, Step sampleDb2FileChunkStep) {
//    return jobBuilderFactory.get("sampleDb2FileChunkJob")
//            .incrementer(new RunIdIncrementer())
//            .listener(jobListener)
//            .flow(sampleDb2FileChunkStep)
//            .end()
//            .build();
//  }


  @Bean
  public JdbcCursorItemReader<Weather> jdbcCursorItemReader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<Weather>()
            .name("jdbcCursorItemReader")
            .fetchSize(100)
            .dataSource(dataSource)
            .rowMapper(new BeanPropertyRowMapper<>(Weather.class))
            .sql("SELECT weather_id, temp, weather, weather_icon, max_temp, min_temp, Date FROM WEATHER_TO_MEMBER")
            .build();
  }

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
