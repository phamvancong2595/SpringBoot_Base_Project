package com.congpv.springboot_base_project.shared.dto.user;

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
