package com.btb.chalKak.batch.listener;

import com.btb.chalKak.domain.weather.service.Impl.WeatherServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TableClearingListener implements JobExecutionListener {

    private final WeatherServiceImpl weatherService;
    private Long seasonId;
    private final DataSource dataSource;

    public Long getSeasonId() {
        return seasonId;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

        seasonId = weatherService.weatherToSeasonId(LocalDate.now());

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM recommend_post");
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        // You can leave this empty if you don't need to do anything after the job
    }
}
