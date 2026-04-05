package com.congpv.springboot_base_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/tasks")
public class TaskController {
    // @PreAuthorize("@projectSecurity.isProjectManager(#projectId, authentication)")
    @PostMapping
    public ResponseEntity<?> createTaskInProject(@PathVariable Long projectId, @RequestBody Object taskRequestDto) {
        return ResponseEntity.ok("Task created successfully");
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long projectId, @PathVariable Long taskId) {
        // Giả lập logic query DB: nếu Task không tồn tại (ví dụ taskId = 999)
        boolean taskExists = false; 
        
        if (!taskExists) {
            // Ném ra Exception đã custom
            throw new com.congpv.springboot_base_project.exception.ResourceNotFoundException("Task not found");
        }
        
        return ResponseEntity.ok("Task data");
    }
}
