package com.congpv.springboot_base_project.core.service.impl;

import com.congpv.springboot_base_project.core.entity.ProjectRole;
import com.congpv.springboot_base_project.core.service.ProjectRoleService;
import com.congpv.springboot_base_project.core.service.ProjectService;
import com.congpv.springboot_base_project.core.service.UserService;
import com.congpv.springboot_base_project.infrastructure.repository.ProjectRoleRepository;
import com.congpv.springboot_base_project.shared.dto.ProjectMemberRequestDto;
import com.congpv.springboot_base_project.shared.dto.ProjectMemberResponseDto;
import com.congpv.springboot_base_project.core.entity.Project;
import com.congpv.springboot_base_project.core.entity.ProjectMember;
import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.shared.exception.BadRequestException;
import com.congpv.springboot_base_project.shared.exception.ResourceNotFoundException;
import com.congpv.springboot_base_project.infrastructure.repository.ProjectMemberRepository;
import com.congpv.springboot_base_project.core.service.ProjectMemberService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectService projectService;
    private final UserService userService;
    private final ProjectRoleService projectRoleService;

    @Override
    @Transactional
    public void addMember(Long projectId, ProjectMemberRequestDto dto) {
        Project project = projectService.findById(projectId);
        User user = userService.getUserByName(dto.username());
        if (projectMemberRepository.existsByProjectIdAndUserId(projectId,user.getId())) {
            throw new BadRequestException("User is already a member of this project");
        }
        ProjectRole role = projectRoleService.getByCode(dto.role());
        ProjectMember member = ProjectMember.builder()
                .project(project)
                .user(user)
                .role(role)
                .build();
        projectMemberRepository.save(member);
    }

    @Override
    public List<ProjectMemberResponseDto> getAllProjectMembers(Long projectId) {
        List<ProjectMember> members = projectMemberRepository.findByProjectId(projectId);
        return members.stream()
                .map(member -> new ProjectMemberResponseDto(
                        member.getUser().getUsername(),
                        member.getRole().getCode()))
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public void save(ProjectMember projectMember) {
        projectMemberRepository.save(projectMember);
    }

}
