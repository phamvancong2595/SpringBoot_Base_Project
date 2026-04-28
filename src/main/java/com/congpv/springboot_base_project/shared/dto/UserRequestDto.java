package com.congpv.springboot_base_project.shared.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record UserRequestDto(
        String username,
        String email,
        String password,
        String fullName,
        List<String> roles,
        Boolean active) {
}
