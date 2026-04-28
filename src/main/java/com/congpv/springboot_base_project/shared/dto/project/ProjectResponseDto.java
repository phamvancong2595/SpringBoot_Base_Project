package com.congpv.springboot_base_project.shared.dto.project;

import lombok.Builder;

@Builder
public record ProjectResponseDto(Long id, String name, String description) {

}
