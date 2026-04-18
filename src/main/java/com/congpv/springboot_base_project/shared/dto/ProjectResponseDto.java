package com.congpv.springboot_base_project.shared.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public record ProjectResponseDto(Long id, String name, String description) {

}
