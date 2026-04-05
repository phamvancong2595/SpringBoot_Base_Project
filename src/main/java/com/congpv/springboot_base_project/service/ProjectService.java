package com.congpv.springboot_base_project.service;

import java.util.List;

import com.congpv.springboot_base_project.dto.ProjectRequestDto;
import com.congpv.springboot_base_project.dto.ProjectResponseDto;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto dto);

    ProjectResponseDto getProjectById(Long projectId);

    List<ProjectResponseDto> getAllProjects();

    ProjectResponseDto updateProject(Long projectId, ProjectRequestDto request);

    void deleteProject(Long projectId);
}
