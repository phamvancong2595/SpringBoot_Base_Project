package com.congpv.springboot_base_project.core.service;

import com.congpv.springboot_base_project.core.entity.Project;
import com.congpv.springboot_base_project.shared.dto.project.ProjectPageResponse;
import com.congpv.springboot_base_project.shared.dto.project.ProjectRequestDto;
import com.congpv.springboot_base_project.shared.dto.project.ProjectResponseDto;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto dto);

    ProjectResponseDto getProjectById(Long projectId);

    ProjectPageResponse getAllProjects(int pageNo, int pageSize);

    ProjectResponseDto updateProject(Long projectId, ProjectRequestDto request);

    void deleteProject(Long projectId);

    Project findById(Long id);

    boolean existById(Long id);
}
