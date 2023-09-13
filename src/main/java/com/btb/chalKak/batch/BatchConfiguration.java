package com.btb.chalKak.batch;

import com.btb.chalKak.batch.listener.TableClearingListener;
import com.btb.chalKak.batch.processor.PostItemProcessor;
import com.btb.chalKak.batch.reader.PostItemReader;
import com.btb.chalKak.batch.writer.RecommendPostItemWriter;
import com.btb.chalKak.domain.batchpost.dto.RecommendPostBatchDTO;
import com.btb.chalKak.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

  private static final int chunkSize = 10000;

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
            .<Post, RecommendPostBatchDTO>chunk(chunkSize)
            .reader(postItemReader)
            .processor(postItemProcessor)
            .writer(recommendPostItemWriter)
            .build();
  }

}
//
//@Configuration
//@Slf4j
//@RequiredArgsConstructor
//public class BatchConfiguration {
//
//  private final JobBuilderFactory jobBuilderFactory;
//  private final StepBuilderFactory stepBuilderFactory;
//  private final RecommendPostItemWriter recommendPostItemWriter;
//  private final TableClearingListener tableClearingListener;
//  private final PostItemProcessor postItemProcessor;
//
//  private final WeatherServiceImpl weatherService
//  private final DataSource dataSource;  // Add this line to get DataSource
//
//  private static final int chunkSize = 10000;
//
//  private Long seasonId = weatherService.weatherToSeasonId(LocalDate.now());
//
//
//
//  // SQL 버전의 JPQL 쿼리
//  private static final String FIND_POSTS_AND_SEASONID_AND_FETCH_SQL =
//          "SELECT p.* " +
//                  "FROM post p " +
//                  "JOIN style_tag s ON s.post_id = p.id " +
//                  "WHERE s.id = ? AND p.status = 'PUBLIC' " +
//                  "GROUP BY p.id " +
//                  "HAVING SUM(CASE WHEN s.id = ? THEN 1 ELSE 0 END) >= 1 " +
//                  "AND (p.view_count + p.like_count) > ?";
//
//  // JdbcCursorItemReader bean definition
//
//
//
//
//  @Bean
//  public JdbcCursorItemReader<Post> postItemReader() {
//
//    return new JdbcCursorItemReaderBuilder<Post>()
//            .dataSource(dataSource)
//            .name("postItemReader")
//            .sql(FIND_POSTS_AND_SEASONID_AND_FETCH_SQL) // Define your SQL query here
//            .preparedStatementSetter(preparedStatementSetter)
//            .rowMapper(new BeanPropertyRowMapper<>(Post.class))
//            .build();
//  }
//
//  @Bean
//  public Job tutorialJob() {
//    return jobBuilderFactory.get("tutorialJob")
//            .start(tutorialStep())
//            .build();
//  }
//
//  @Bean
//  public Step tutorialStep() {
//    return stepBuilderFactory.get("tutorialStep")
//            .tasklet(new TutorialTasklet())
//            .build();
//  }
//
//  @Bean
//  public Job processJob() {
//    return jobBuilderFactory.get("processJob")
//            .listener(tableClearingListener)
//            .flow(orderStep1())
//            .end()
//            .build();
//  }
//
//  @Bean
//  public Step orderStep1() {
//    return stepBuilderFactory.get("orderStep1")
//            .<Post, RecommendPostBatchDTO>chunk(chunkSize)
//            .reader(postItemReader()) // Using JdbcCursorItemReader bean
//            .processor(postItemProcessor)
//            .writer(recommendPostItemWriter)
//            .build();
//  }
//}
