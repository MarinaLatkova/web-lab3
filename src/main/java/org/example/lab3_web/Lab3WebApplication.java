package org.example.lab3_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lab3WebApplication {

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.home"));
        SpringApplication.run(Lab3WebApplication.class, args);
    }

}
