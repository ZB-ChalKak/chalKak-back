package com.btb.chalKak.batch.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
@RequiredArgsConstructor
public class TableClearingListener implements JobExecutionListener {


    private final DataSource dataSource;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM weather_to_member");
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        // You can leave this empty if you don't need to do anything after the job
    }
}
