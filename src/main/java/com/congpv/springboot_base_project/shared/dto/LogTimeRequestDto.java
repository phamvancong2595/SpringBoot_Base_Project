package com.congpv.springboot_base_project.shared.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record LogTimeRequestDto(
        @NotNull(message = "Id of task cannot blank")
        Long taskId,
        @NotNull(message = "Hours spent in task cannot blank")
        @Positive(message = "Hour spent must greater than 0")
        Double hourSpent,

        String description,
        @NotNull(message = "Logtime date cannot blank")
        LocalDate localDate
) {

}
