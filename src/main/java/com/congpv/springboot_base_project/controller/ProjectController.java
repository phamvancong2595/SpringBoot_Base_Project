package com.congpv.springboot_base_project.controller;

import com.congpv.springboot_base_project.dto.ApiResponse;
import com.congpv.springboot_base_project.dto.ProjectRequestDto;
import com.congpv.springboot_base_project.dto.ProjectResponseDto;
import com.congpv.springboot_base_project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

}
