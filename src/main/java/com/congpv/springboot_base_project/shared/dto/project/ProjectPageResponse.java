package com.congpv.springboot_base_project.shared.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPageResponse {
    private List<ProjectResponseDto> content;
    private int totalPages;
    private int totalElements;
    private int page;
    private int size;
    private boolean isLast;
}
