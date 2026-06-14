package com.example.taskdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Spring Boot application.
 * Running this class starts the embedded web server (default: http://localhost:8080).
 */
@SpringBootApplication
public class TaskDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskDemoApplication.class, args);
    }
}
