package com.congpv.springboot_base_project.application.controller;

import com.congpv.springboot_base_project.shared.dto.ApiResponse;
import com.congpv.springboot_base_project.shared.dto.PageResponse;
import com.congpv.springboot_base_project.shared.dto.ProjectResponseDto;
import com.congpv.springboot_base_project.shared.dto.TaskRequestDto;
import com.congpv.springboot_base_project.shared.dto.TaskResponseDto;
import com.congpv.springboot_base_project.core.entity.Task;
import com.congpv.springboot_base_project.core.service.EmailService;
import com.congpv.springboot_base_project.core.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final EmailService emailService;

    @PreAuthorize("@projectSecurity.isProjectMember(#projectId, authentication)")
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponseDto>> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody TaskRequestDto request,
            Authentication authentication) {
        String reporterUsername = authentication.getName();
        TaskResponseDto task = taskService.createTask(projectId, request, reporterUsername);
        emailService.sendNewTaskNotification("congpv24@gmail.com", task.title(), reporterUsername);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(task));
    }

    @PreAuthorize("@projectSecurity.isProjectMember(#projectId, authentication)")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TaskResponseDto>>> getAllTasks(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @PathVariable Long projectId) {
        PageResponse<TaskResponseDto> response = taskService.getTasksByProject(projectId, page, size);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách công việc thành công", response));
    }

    @PreAuthorize("@projectSecurity.isProjectMember(#projectId, authentication)")
    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDto>> getTaskById(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        TaskResponseDto task = taskService.getTaskById(projectId, taskId);
        return ResponseEntity.ok(ApiResponse.success(task));
    }

    @PreAuthorize("@projectSecurity.isProjectMember(#projectId, authentication)")
    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDto>> updateTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequestDto request) {
        TaskResponseDto task = taskService.updateTask(projectId, taskId, request);
        return ResponseEntity.ok(ApiResponse.success("Task updated", task));
    }

    @PreAuthorize("@projectSecurity.isProjectManager(#projectId, authentication)")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        taskService.deleteTask(projectId, taskId);
        return ResponseEntity.ok(ApiResponse.success("Task deleted", null));
    }
}
