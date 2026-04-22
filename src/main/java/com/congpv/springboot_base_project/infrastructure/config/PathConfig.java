package com.congpv.springboot_base_project.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathConfig {
    @Bean
    public String[] publicPaths() {
        return new String[]{
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/api-docs/**",
                "/api/v1/auth/**"
        };
    }

    @Bean(name = "adminPaths")
    public String[] adminPaths() {
        return new String[]{

        };
    }

    @Bean(name = "securedPaths")
    public String[] securedPaths() {
        return new String[]{
                "/api/**"
        };
    }
}
