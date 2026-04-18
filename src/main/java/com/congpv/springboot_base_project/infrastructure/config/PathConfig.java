package com.congpv.springboot_base_project.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
