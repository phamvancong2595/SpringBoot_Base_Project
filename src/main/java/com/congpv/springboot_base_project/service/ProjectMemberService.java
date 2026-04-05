package com.congpv.springboot_base_project.service;

import com.congpv.springboot_base_project.dto.ProjectMemberRequestDto;

public interface ProjectMemberService {
    public void addMember(Long projectId, ProjectMemberRequestDto dto);
}
