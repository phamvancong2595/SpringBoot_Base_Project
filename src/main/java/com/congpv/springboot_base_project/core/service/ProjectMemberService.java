package com.congpv.springboot_base_project.core.service;

import java.util.List;

import com.congpv.springboot_base_project.shared.dto.ProjectMemberRequestDto;
import com.congpv.springboot_base_project.shared.dto.ProjectMemberResponseDto;

public interface ProjectMemberService {
    public void addMember(Long projectId, ProjectMemberRequestDto dto);

    public List<ProjectMemberResponseDto> getAllProjectMembers(Long projectId);
}
