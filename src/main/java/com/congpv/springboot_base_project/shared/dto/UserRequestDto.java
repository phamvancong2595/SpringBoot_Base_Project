package com.congpv.springboot_base_project.shared.dto;

import com.congpv.springboot_base_project.shared.enums.UserRole;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRequestDto (
     String username,
    @Size(max = 100)
     String email,
     String password,
     String fullName,
     UserRole role,
     Boolean active
    )
            {
}
