package com.congpv.springboot_base_project.infrastructure.security;

import com.congpv.springboot_base_project.core.entity.ProjectMember;
import com.congpv.springboot_base_project.core.repository.ProjectMemberRepository;
import com.congpv.springboot_base_project.shared.constant.ApplicationConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service("projectSecurity")
@RequiredArgsConstructor
public class ProjectSecurityService {
    private final ProjectMemberRepository projectMemberRepository;

    public boolean isProjectManager(Long projectId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        String username = authentication.getName();
        Optional<ProjectMember> member = projectMemberRepository.findByProjectIdAndUserUsername(projectId, username);
        return member.isPresent() && Objects.equals(member.get().getRole().getCode(), ApplicationConstants.MANAGER);
    }

    public boolean isProjectMember(Long projectId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        String username = authentication.getName();
        Optional<ProjectMember> member = projectMemberRepository.findByProjectIdAndUserUsername(projectId, username);
        return member.isPresent();
    }
}
