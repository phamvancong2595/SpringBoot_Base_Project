package com.congpv.springboot_base_project.core.service.impl;

import com.congpv.springboot_base_project.core.entity.ProjectRole;
import com.congpv.springboot_base_project.core.service.ProjectMemberService;
import com.congpv.springboot_base_project.core.service.ProjectRoleService;
import com.congpv.springboot_base_project.infrastructure.repository.ProjectRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectRoleServiceImpl implements ProjectRoleService {
private final ProjectRoleRepository projectRoleRepository;

    @Override
    public ProjectRole getByCode(String code) {
        return projectRoleRepository.findByCode(code);
    }
}
