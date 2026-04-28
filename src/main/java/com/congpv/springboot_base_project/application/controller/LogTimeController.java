package com.congpv.springboot_base_project.application.controller;

import com.congpv.springboot_base_project.core.service.LogtimeService;
import com.congpv.springboot_base_project.shared.dto.logtime.LogTimeRequestDto;
import com.congpv.springboot_base_project.shared.dto.logtime.LogTimeResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logtimes")
@RequiredArgsConstructor
public class LogTimeController {
    private final LogtimeService logtimeService;
    @PostMapping
    public ResponseEntity<LogTimeResponseDto> createLogtime(@Valid @RequestBody LogTimeRequestDto request) {
        LogTimeResponseDto response = logtimeService.createLogtime(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
