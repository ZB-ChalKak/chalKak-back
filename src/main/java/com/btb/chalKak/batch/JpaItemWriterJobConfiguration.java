package com.btb.chalKak.batch;

import com.btb.chalKak.batch.listener.TableClearingListener;
import com.btb.chalKak.common.exception.PostException;
import com.btb.chalKak.domain.batchpost.entity.RecommendPostBatch;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.type.StyleCategory;
import com.btb.chalKak.domain.weather.service.Impl.WeatherServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.btb.chalKak.common.exception.type.ErrorCode.NOT_FOUND_STYLETAG_KEYWORD;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaItemWriterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private final TableClearingListener tableClearingListener;

    private final WeatherServiceImpl weatherService;

    private static final String weather_SQL = "SELECT p FROM Post p " +
            "JOIN p.styleTags s " +
            "WHERE ( s.id = :seasonId ) and p.status = 'PUBLIC' " +
            "GROUP BY p " +
            "HAVING SUM(CASE WHEN s.id = :seasonId THEN 1 ELSE 0 END) >= 1 " +
            "AND (p.viewCount + p.likeCount) > :count ";
    private static final int chunkSize = 10;

    @Bean
    public Job jpaItemWriterJob() {
        return jobBuilderFactory.get("jpaItemWriterJob")
                .start(jpaItemWriterStep())
                .build();
    }

    @Bean
    public Step jpaItemWriterStep() {
        return stepBuilderFactory.get("jpaItemWriterStep")
                .<Post, RecommendPostBatch>chunk(chunkSize)
                .listener(tableClearingListener)
                .reader(jpaItemWriterReader())
                .processor(jpaItemProcessor())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Post> jpaItemWriterReader() {

        Map<String, Object> params = new HashMap<>();
        Long seasonId = weatherService.weatherToSeasonId(LocalDate.now());
        params.put("seasonId", seasonId);
        params.put("count", 1L);

        return new JpaPagingItemReaderBuilder<Post>()
                .name("jpaItemWriterReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString(weather_SQL)
                .parameterValues(params)
                .build();
    }

    @Bean
    public ItemProcessor<Post, RecommendPostBatch> jpaItemProcessor() {
        return post -> RecommendPostBatch.builder()
                .id(post.getId())
                .weatherId(post.getStyleTags().stream()
                        .filter(styleTag -> styleTag.getCategory() == StyleCategory.WEATHER)
                        .map(StyleTag::getId)
                        .findFirst()
                        .orElseThrow(()->new PostException(NOT_FOUND_STYLETAG_KEYWORD))
                )
                .seasonId(post.getStyleTags().stream()
                        .filter(styleTag -> styleTag.getCategory() == StyleCategory.SEASON)
                        .map(StyleTag::getId)
                        .findFirst()
                        .orElseThrow(()->new PostException(NOT_FOUND_STYLETAG_KEYWORD)))
                .styleTagIds(post.getStyleTags().stream()
                        .filter(styleTag -> (styleTag.getCategory() == StyleCategory.STYLE || styleTag.getCategory() == StyleCategory.TPO))
                        .map(StyleTag::getId)
                        .map(Object::toString)
                        .collect(Collectors.joining(",")))
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .build();
    }

    @Bean
    public JpaItemWriter<RecommendPostBatch> jpaItemWriter() {
        JpaItemWriter<RecommendPostBatch> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

    @Bean
    public ItemWriter<RecommendPostBatch> customItemWriter() {
        return new ItemWriter<RecommendPostBatch>() {
            @Override
            public void write(List<? extends RecommendPostBatch> items) throws Exception {
                for (RecommendPostBatch item : items) {
                    System.out.println(item.getId());
                }
            }
        };
    }
}