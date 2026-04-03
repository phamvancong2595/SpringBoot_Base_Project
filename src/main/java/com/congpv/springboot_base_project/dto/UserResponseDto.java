package com.congpv.springboot_base_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Boolean active;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
