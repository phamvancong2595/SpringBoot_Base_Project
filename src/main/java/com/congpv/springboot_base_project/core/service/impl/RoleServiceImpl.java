package com.congpv.springboot_base_project.core.service.impl;

import com.congpv.springboot_base_project.core.entity.Role;
import com.congpv.springboot_base_project.core.entity.Task;
import com.congpv.springboot_base_project.core.service.RoleService;
import com.congpv.springboot_base_project.infrastructure.repository.RoleRepository;
import com.congpv.springboot_base_project.shared.dto.TaskResponseDto;
import com.congpv.springboot_base_project.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByCode(String code) {
        return roleRepository.findByCode(code);
    }

    @Override
    public Set<Role> getRolesByCodes(List<String> codes) {
        return roleRepository.findByCodeIn(codes);
    }
}
