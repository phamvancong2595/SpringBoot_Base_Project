package com.congpv.springboot_base_project.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Mini Jira API", version = "1.0", description = "API Documentation cho dự án quản lý task Mini Jira", contact = @Contact(name = "Cong PV")),
        // Áp dụng bảo mật JWT này cho toàn bộ API (ngoại trừ những cái public)
        security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", description = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjb25ncGhhbTEyMzQiLCJpYXQiOjE3NzU0NjU3NzksImV4cCI6MTc3NjA3MDU3OX0.8AJ9JLj9q7OXzwpqJtjceRtuU765FTwRZr61Ueif5bA")
public class OpenApiConfig {
}