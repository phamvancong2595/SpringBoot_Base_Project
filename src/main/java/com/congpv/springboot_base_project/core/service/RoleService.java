package com.congpv.springboot_base_project.core.service;

import com.congpv.springboot_base_project.core.entity.Role;

public interface RoleService {
    Role getRoleByCode(String code);
}
