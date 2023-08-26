package com.btb.chalKak;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@EnableBatchProcessing
@SpringBootApplication
public class ChalKakApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChalKakApplication.class, args);
    }

}
