package com.sys.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HoraryApplication {
    public static void main(String[] args) {
        SpringApplication.run(HoraryApplication.class, args);
    }
}
