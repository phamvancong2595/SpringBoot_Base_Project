package com.congpv.springboot_base_project.core.service.impl;

import com.congpv.springboot_base_project.core.entity.ProjectRole;
import com.congpv.springboot_base_project.core.service.ProjectRoleService;
import com.congpv.springboot_base_project.core.service.UserService;
import com.congpv.springboot_base_project.core.repository.ProjectMemberRepository;
import com.congpv.springboot_base_project.shared.constant.ApplicationConstants;
import com.congpv.springboot_base_project.shared.dto.common.PageResponse;
import com.congpv.springboot_base_project.shared.dto.project.ProjectPageResponse;
import com.congpv.springboot_base_project.shared.dto.project.ProjectRequestDto;
import com.congpv.springboot_base_project.shared.dto.project.ProjectResponseDto;
import com.congpv.springboot_base_project.core.entity.Project;
import com.congpv.springboot_base_project.core.entity.ProjectMember;
import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.shared.exception.ResourceNotFoundException;
import com.congpv.springboot_base_project.core.repository.ProjectRepository;
import com.congpv.springboot_base_project.core.service.ProjectService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserService userService;
    private final ProjectRoleService projectRoleService;

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "projects_pagination", allEntries = true)
    })
    public ProjectResponseDto createProject(ProjectRequestDto dto) {
        // 1. Tạo Project
        Project project = Project.builder()
                .name(dto.name())
                .description(dto.description())
                .build();
        Project saved = projectRepository.save(project);

        // 2. Lấy username của người đang đăng nhập từ JWT
        String currentUsername = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User creator = userService.getUserByName(currentUsername);

        // 3. Tự động gán người tạo làm PROJECT_MANAGER
        ProjectRole role = projectRoleService.getByCode(ApplicationConstants.MANAGER);
        ProjectMember manager = ProjectMember.builder()
                .project(saved)
                .user(creator)
                .role(role)
                .build();
        projectMemberRepository.save(manager);

        return ProjectResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .build();
    }

    public Project getProject(Long id) {
        return projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
    }

    @Override
    @Cacheable(value = "projects_details", key = "#projectId")
    public ProjectResponseDto getProjectById(Long projectId) {
        Project project = projectRepository.findByIdAndIsDeletedFalse(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        return ProjectResponseDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .build();
    }

    @Override
    @Cacheable(value = "projects_pagination", key = "#pageNo + '-' + #pageSize")
    public ProjectPageResponse getAllProjects(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());

        Page<Project> projectPage = projectRepository.findAllByIsDeletedFalse(pageable);

        List<ProjectResponseDto> content = projectPage.getContent().stream()
                .map(project -> ProjectResponseDto.builder()
                        .id(project.getId())
                        .name(project.getName())
                        .description(project.getDescription())
                        .build())
                .collect(Collectors.toList());
        return new ProjectPageResponse(
                content,
                projectPage.getNumber(),
                projectPage.getSize(),
                projectPage.getTotalElements(),
                projectPage.getTotalPages(),
                projectPage.isLast()
        );
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "project_details", key = "#projectId"),
            @CacheEvict(value = "projects_pagination", allEntries = true)
    })
    public ProjectResponseDto updateProject(Long projectId, ProjectRequestDto request) {
        Project project = getProject(projectId);
        if (Strings.isNotBlank(request.name())) {
            project.setName(request.name());
        }
        if (Strings.isNotBlank(request.description())) {
            project.setDescription(request.description());
        }
        if (Strings.isNotBlank(request.name()) || Strings.isNotBlank(request.description())) {
            Project saved = projectRepository.save(project);
            return ProjectResponseDto.builder()
                    .id(saved.getId())
                    .name(saved.getName())
                    .description(saved.getDescription())
                    .build();
        }
        return null;
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "project_details", key = "#projectId"),
            @CacheEvict(value = "projects_pagination", allEntries = true)
    })
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findByIdAndIsDeletedFalse(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        if (project != null) {
            project.setDeleted(true);
            projectRepository.save(project);
        }

    }

    @Override
    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project", "Id", id));
    }

    @Override
    public boolean existById(Long id) {
        return projectRepository.existsById(id);
    }

}
