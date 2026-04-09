package com.congpv.springboot_base_project.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.congpv.springboot_base_project.shared.enums.UserRole;

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
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
