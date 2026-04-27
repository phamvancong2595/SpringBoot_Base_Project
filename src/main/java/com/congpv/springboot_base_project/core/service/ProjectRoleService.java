package com.congpv.springboot_base_project.core.service;

import com.congpv.springboot_base_project.core.entity.ProjectRole;

public interface ProjectRoleService {
    ProjectRole getByCode(String code);
}
