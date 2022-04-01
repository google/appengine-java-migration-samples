package com.example.appengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
@EnableAutoConfiguration
public class MigratedServletApplication {
    public static void main(String[] args) {
        SpringApplication.run(MigratedServletApplication.class, args);
    }
}
