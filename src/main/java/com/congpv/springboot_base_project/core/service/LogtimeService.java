package com.congpv.springboot_base_project.core.service;

import com.congpv.springboot_base_project.shared.dto.LogTimeRequestDto;
import com.congpv.springboot_base_project.shared.dto.LogTimeResponseDto;
import jakarta.validation.Valid;

public interface LogtimeService {
    LogTimeResponseDto createLogtime(LogTimeRequestDto request);
}
