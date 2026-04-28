package com.congpv.springboot_base_project.core.service;

import com.congpv.springboot_base_project.shared.dto.logtime.LogTimeRequestDto;
import com.congpv.springboot_base_project.shared.dto.logtime.LogTimeResponseDto;

public interface LogtimeService {
    LogTimeResponseDto createLogtime(LogTimeRequestDto request);
}
