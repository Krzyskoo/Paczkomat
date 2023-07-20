package com.example.notatnik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PaczkomatApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaczkomatApplication.class, args);
    }

}
