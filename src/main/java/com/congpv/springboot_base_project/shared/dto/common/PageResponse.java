package com.congpv.springboot_base_project.shared.dto.common;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean isLast
) {
}
