package com.congpv.springboot_base_project.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectResponseDto {
    private Long id;
    private String name;
    private String description;
}
