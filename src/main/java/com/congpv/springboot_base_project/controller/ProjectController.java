package com.congpv.springboot_base_project.controller;

import com.congpv.springboot_base_project.dto.ApiResponse;
import com.congpv.springboot_base_project.dto.PageResponse;
import com.congpv.springboot_base_project.dto.ProjectMemberRequestDto;
import com.congpv.springboot_base_project.dto.ProjectMemberResponseDto;
import com.congpv.springboot_base_project.dto.ProjectRequestDto;
import com.congpv.springboot_base_project.dto.ProjectResponseDto;
import com.congpv.springboot_base_project.dto.TaskRequestDto;
import com.congpv.springboot_base_project.dto.TaskResponseDto;
import com.congpv.springboot_base_project.dto.UserResponseDto;
import com.congpv.springboot_base_project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponseDto>> createProject(
            @Valid @RequestBody ProjectRequestDto request) {
        ProjectResponseDto dto = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(dto));
    }

    @PreAuthorize("@projectSecurity.isProjectMember(#projectId, authentication)")
    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> getProjectById(
            @PathVariable Long projectId) {
        ProjectResponseDto project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(ApiResponse.success(project));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProjectResponseDto>>> getAllProjects(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        PageResponse<ProjectResponseDto> projects = projectService.getAllProjects(page, size);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách dự án thành công", projects));
    }

    @PreAuthorize("@projectSecurity.isProjectManager(#projectId, authentication)")
    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequestDto request) {
        ProjectResponseDto project = projectService.updateProject(projectId, request);
        return ResponseEntity.ok(ApiResponse.success("Project updated", project));
    }

    @PreAuthorize("hasRole('ADMIN') or @projectSecurity.isProjectManager(#projectId, authentication)")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok(ApiResponse.success("Project deleted", null));

    }
}
