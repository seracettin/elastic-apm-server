package com.example.apmtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for APM testing.
 * This application provides endpoints to test Elastic APM monitoring capabilities.
 */
@SpringBootApplication
public class ApmTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApmTestApplication.class, args);
        System.out.println("===========================================");
        System.out.println("APM Test Application started successfully!");
        System.out.println("Fast endpoint: http://localhost:8080/api/fast");
        System.out.println("Slow endpoint: http://localhost:8080/api/slow");
        System.out.println("Health check: http://localhost:8080/actuator/health");
        System.out.println("===========================================");
    }
}
