package com.foodbridge.foodbridgebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FoodbridgeBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodbridgeBackendApplication.class, args);
    }
}