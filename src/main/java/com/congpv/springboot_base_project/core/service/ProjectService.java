package com.congpv.springboot_base_project.core.service;

import com.congpv.springboot_base_project.core.entity.Project;
import com.congpv.springboot_base_project.shared.dto.PageResponse;
import com.congpv.springboot_base_project.shared.dto.ProjectRequestDto;
import com.congpv.springboot_base_project.shared.dto.ProjectResponseDto;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto dto);

    ProjectResponseDto getProjectById(Long projectId);

    PageResponse<ProjectResponseDto> getAllProjects(int pageNo, int pageSize);

    ProjectResponseDto updateProject(Long projectId, ProjectRequestDto request);

    void deleteProject(Long projectId);

    Project findById(Long id);

    boolean existById(Long id);
}
