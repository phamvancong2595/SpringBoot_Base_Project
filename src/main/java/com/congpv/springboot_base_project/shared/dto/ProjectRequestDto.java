package com.congpv.springboot_base_project.shared.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectRequestDto(
        @NotBlank(message = "project title cannot blank")
        String name,
        @Size(max = 500, message = "description must below 500 character")
        String description) {

}
