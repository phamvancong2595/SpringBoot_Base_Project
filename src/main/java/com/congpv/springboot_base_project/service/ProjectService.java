package com.congpv.springboot_base_project.service;

import com.congpv.springboot_base_project.dto.PageResponse;
import com.congpv.springboot_base_project.dto.ProjectRequestDto;
import com.congpv.springboot_base_project.dto.ProjectResponseDto;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto dto);

    ProjectResponseDto getProjectById(Long projectId);

    PageResponse<ProjectResponseDto> getAllProjects(int pageNo, int pageSize);

    ProjectResponseDto updateProject(Long projectId, ProjectRequestDto request);

    void deleteProject(Long projectId);
}
