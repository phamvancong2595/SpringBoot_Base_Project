package com.congpv.springboot_base_project.shared.dto;

import com.congpv.springboot_base_project.shared.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRequestDto(
        @NotBlank(message = "username cannot blank")
        @Size(min = 5, max = 20, message = "name must between 5 to 20 character")
        String username,
        @Email(message = "Invalid email")
        String email,
        @NotBlank(message = "password cannot blank")
        @Size(min = 8, max = 20, message = "password must between 8 to 20 character")
        String password,
        String fullName,
        UserRole role,
        Boolean active) {
}
