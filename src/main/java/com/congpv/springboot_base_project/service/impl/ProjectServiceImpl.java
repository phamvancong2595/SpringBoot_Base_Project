package com.congpv.springboot_base_project.service.impl;

import com.congpv.springboot_base_project.dto.ProjectRequestDto;
import com.congpv.springboot_base_project.dto.ProjectResponseDto;
import com.congpv.springboot_base_project.entity.Project;
import com.congpv.springboot_base_project.entity.ProjectMember;
import com.congpv.springboot_base_project.entity.User;
import com.congpv.springboot_base_project.enums.ProjectRole;
import com.congpv.springboot_base_project.exception.ResourceNotFoundException;
import com.congpv.springboot_base_project.repository.ProjectMemberRepository;
import com.congpv.springboot_base_project.repository.ProjectRepository;
import com.congpv.springboot_base_project.repository.UserRepository;
import com.congpv.springboot_base_project.service.ProjectService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto dto) {
        // 1. Tạo Project
        Project project = Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        Project saved = projectRepository.save(project);

        // 2. Lấy username của người đang đăng nhập từ JWT
        String currentUsername = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User creator = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUsername));

        // 3. Tự động gán người tạo làm PROJECT_MANAGER
        ProjectMember manager = ProjectMember.builder()
                .project(saved)
                .user(creator)
                .role(ProjectRole.PROJECT_MANAGER)
                .build();
        projectMemberRepository.save(manager);

        return ProjectResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .build();
    }

    public Project getProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
    }
}
