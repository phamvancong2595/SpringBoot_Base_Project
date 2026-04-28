package com.congpv.springboot_base_project.shared.dto.logtime;

import lombok.Builder;

import java.time.LocalDate;
@Builder
public record LogTimeResponseDto(
        Long id,
        Double hoursSpent,
        String description,
        LocalDate logDate,
        Long taskId,
        Long userId,
        String createdBy
) {
}
