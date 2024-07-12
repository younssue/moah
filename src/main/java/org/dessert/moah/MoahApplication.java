package org.dessert.moah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableRetry
public class MoahApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoahApplication.class, args);
    }

}
