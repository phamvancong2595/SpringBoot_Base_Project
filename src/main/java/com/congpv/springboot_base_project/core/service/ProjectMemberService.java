package com.congpv.springboot_base_project.core.service;

import java.util.List;

import com.congpv.springboot_base_project.core.entity.ProjectMember;
import com.congpv.springboot_base_project.shared.dto.project_member.ProjectMemberRequestDto;
import com.congpv.springboot_base_project.shared.dto.project_member.ProjectMemberResponseDto;

public interface ProjectMemberService {
    public void addMember(Long projectId, ProjectMemberRequestDto dto);

    public List<ProjectMemberResponseDto> getAllProjectMembers(Long projectId);

    void save(ProjectMember projectMember);

    void deleteMemberOfProject(Long projectId, Long memberId);
}
