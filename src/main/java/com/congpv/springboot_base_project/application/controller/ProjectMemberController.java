package com.congpv.springboot_base_project.application.controller;

import com.congpv.springboot_base_project.shared.dto.common.ApiResponse;
import com.congpv.springboot_base_project.shared.dto.project_member.ProjectMemberRequestDto;
import com.congpv.springboot_base_project.shared.dto.project_member.ProjectMemberResponseDto;
import com.congpv.springboot_base_project.core.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

    @PreAuthorize("@projectSecurity.isProjectMember(#projectId, authentication)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectMemberResponseDto>>> getAllProjectMembers(
            @PathVariable Long projectId) {
        List<ProjectMemberResponseDto> projectMembers = memberService.getAllProjectMembers(projectId);
        return ResponseEntity.ok(ApiResponse.success(projectMembers));
    }

    @PreAuthorize("@projectSecurity.isProjectManager(#projectId, authentication)")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Void>> deleteProjectMember(
            @PathVariable Long projectId, @PathVariable Long memberId) {
        memberService.deleteMemberOfProject(projectId, memberId);
        return ResponseEntity.ok(ApiResponse.success("Member delete successful", null));
    }
}
