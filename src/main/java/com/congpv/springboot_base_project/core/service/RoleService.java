package com.congpv.springboot_base_project.core.service;

import com.congpv.springboot_base_project.core.entity.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Role getRoleByCode(String code);

    Set<Role> getRolesByCodes(List<String> codes);
}
