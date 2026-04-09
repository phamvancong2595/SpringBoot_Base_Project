package com.congpv.springboot_base_project.shared.dto;

import com.congpv.springboot_base_project.shared.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private String username;

    @Size(max = 100)
    private String email;

    private String password;

    private String fullName;
    private UserRole role;
    private Boolean active;
}
