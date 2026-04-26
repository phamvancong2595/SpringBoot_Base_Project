package com.congpv.springboot_base_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.congpv.springboot_base_project")
public class SpringbootBaseProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootBaseProjectApplication.class, args);
    }
}
