package com.congpv.springboot_base_project.service;

import java.util.List;

import com.congpv.springboot_base_project.dto.ProjectMemberRequestDto;
import com.congpv.springboot_base_project.dto.ProjectMemberResponseDto;

public interface ProjectMemberService {
    public void addMember(Long projectId, ProjectMemberRequestDto dto);

    public List<ProjectMemberResponseDto> getAllProjectMembers(Long projectId);
}
