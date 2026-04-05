package com.congpv.springboot_base_project.controller;

import com.congpv.springboot_base_project.dto.ApiResponse;
import com.congpv.springboot_base_project.dto.TaskRequestDto;
import com.congpv.springboot_base_project.dto.TaskResponseDto;
import com.congpv.springboot_base_project.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponseDto>> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody TaskRequestDto request,
            Authentication authentication) {
        String reporterUsername = authentication.getName();
        TaskResponseDto task = taskService.createTask(projectId, request, reporterUsername);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(task));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getAllTasks(@PathVariable Long projectId) {
        List<TaskResponseDto> tasks = taskService.getTasksByProject(projectId);
        return ResponseEntity.ok(ApiResponse.success(tasks));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDto>> getTaskById(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        TaskResponseDto task = taskService.getTaskById(projectId, taskId);
        return ResponseEntity.ok(ApiResponse.success(task));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDto>> updateTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequestDto request) {
        TaskResponseDto task = taskService.updateTask(projectId, taskId, request);
        return ResponseEntity.ok(ApiResponse.success("Task updated", task));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        taskService.deleteTask(projectId, taskId);
        return ResponseEntity.ok(ApiResponse.success("Task deleted", null));
    }
}
