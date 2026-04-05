package com.congpv.springboot_base_project.controller;

import com.congpv.springboot_base_project.dto.ApiResponse;
import com.congpv.springboot_base_project.dto.ProjectMemberRequestDto;
import com.congpv.springboot_base_project.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/members")
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService memberService;

    // Chỉ người quản lý dự án (Project Manager) mới được gán thành viên
    @PreAuthorize("@projectSecurity.isProjectManager(#projectId, authentication)")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addMember(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectMemberRequestDto request) {
        memberService.addMember(projectId, request);
        return ResponseEntity.ok(ApiResponse.success("Member added", null));
    }
}
