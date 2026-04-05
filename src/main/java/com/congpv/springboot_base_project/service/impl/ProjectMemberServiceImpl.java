package com.congpv.springboot_base_project.service.impl;

import com.congpv.springboot_base_project.dto.ProjectMemberRequestDto;
import com.congpv.springboot_base_project.entity.Project;
import com.congpv.springboot_base_project.entity.ProjectMember;
import com.congpv.springboot_base_project.entity.User;
import com.congpv.springboot_base_project.enums.ProjectRole;
import com.congpv.springboot_base_project.exception.ResourceNotFoundException;
import com.congpv.springboot_base_project.repository.ProjectMemberRepository;
import com.congpv.springboot_base_project.repository.ProjectRepository;
import com.congpv.springboot_base_project.repository.UserRepository;
import com.congpv.springboot_base_project.service.ProjectMemberService;

import lombok.RequiredArgsConstructor;
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
        User user = userRepo.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", dto.getUsername()));

        ProjectMember member = ProjectMember.builder()
                .project(project)
                .user(user)
                .role(ProjectRole.valueOf(dto.getRole()))
                .build();
        memberRepo.save(member);
    }

}
