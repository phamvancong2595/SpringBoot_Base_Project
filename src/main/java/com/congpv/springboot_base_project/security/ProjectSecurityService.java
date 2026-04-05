package com.congpv.springboot_base_project.security;

import com.congpv.springboot_base_project.entity.ProjectMember;
import com.congpv.springboot_base_project.enums.ProjectRole;
import com.congpv.springboot_base_project.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
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
        return member.isPresent() && member.get().getRole() == ProjectRole.PROJECT_MANAGER;
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
