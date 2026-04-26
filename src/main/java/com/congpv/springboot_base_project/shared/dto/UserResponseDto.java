package com.congpv.springboot_base_project.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserResponseDto(
        Long id,
        String username,
        String email,
        String fullName,
        Boolean active,
        List<String> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
