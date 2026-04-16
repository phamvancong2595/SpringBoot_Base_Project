package com.congpv.springboot_base_project.core.service.impl;

import com.congpv.springboot_base_project.shared.dto.ProjectMemberRequestDto;
import com.congpv.springboot_base_project.shared.dto.ProjectMemberResponseDto;
import com.congpv.springboot_base_project.core.entity.Project;
import com.congpv.springboot_base_project.core.entity.ProjectMember;
import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.shared.enums.ProjectRole;
import com.congpv.springboot_base_project.shared.exception.ResourceNotFoundException;
import com.congpv.springboot_base_project.infrastructure.repository.ProjectMemberRepository;
import com.congpv.springboot_base_project.infrastructure.repository.ProjectRepository;
import com.congpv.springboot_base_project.infrastructure.repository.UserRepository;
import com.congpv.springboot_base_project.core.service.ProjectMemberService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {

        private final ProjectMemberRepository memberRepo;
        private final ProjectRepository projectRepo;
        private final UserRepository userRepo;

        @Override
        public void addMember(Long projectId, ProjectMemberRequestDto dto) {
                Project project = projectRepo.findById(projectId)
                                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
                User user = userRepo.findByUsername(dto.username())
                                .orElseThrow(() -> new ResourceNotFoundException("User", "username",
                                                dto.username()));

                ProjectMember member = ProjectMember.builder()
                                .project(project)
                                .user(user)
                                .role(ProjectRole.valueOf(dto.role()))
                                .build();
                memberRepo.save(member);
        }

        @Override
        public List<ProjectMemberResponseDto> getAllProjectMembers(Long projectId) {
                List<ProjectMember> members = memberRepo.findByProjectId(projectId);
                return members.stream()
                                .map(member -> new ProjectMemberResponseDto(
                                                member.getUser().getUsername(),
                                                member.getRole().name()))
                                .collect(Collectors.toList());

        }

}
