package com.korit.backend_mini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KoritGovBackendMiniApplication {

    public static void main(String[] args) {
        SpringApplication.run(KoritGovBackendMiniApplication.class, args);
    }

}
