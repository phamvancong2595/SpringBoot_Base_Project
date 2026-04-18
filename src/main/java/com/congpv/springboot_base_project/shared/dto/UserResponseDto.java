package com.congpv.springboot_base_project.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.congpv.springboot_base_project.shared.enums.UserRole;

@Builder
public record UserResponseDto(
        Long id,
        String username,
        String email,
        String fullName,
        Boolean active,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
