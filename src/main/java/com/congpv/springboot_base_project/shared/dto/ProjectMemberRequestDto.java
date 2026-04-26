package com.congpv.springboot_base_project.shared.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProjectMemberRequestDto(
        @NotBlank(message = "User name cannot blank")
        String username,
        @NotBlank(message = "role cannot blank")
        @Pattern(regexp = "PROJECT_MANAGER|DEVELOPER|TESTER|BRIDGE_ENGINEER|PQA|LEADER", message = "role must be one of: PROJECT_MANAGER|DEVELOPER|TESTER|VIEWER")
        String role) {
}
