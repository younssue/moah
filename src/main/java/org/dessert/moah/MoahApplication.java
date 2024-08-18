package org.dessert.moah;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableRetry
@EnableAsync
public class MoahApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(MoahApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
