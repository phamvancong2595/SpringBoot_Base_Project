package com.congpv.springboot_base_project.service;

import com.congpv.springboot_base_project.dto.ProjectRequestDto;
import com.congpv.springboot_base_project.dto.ProjectResponseDto;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto dto);
}
