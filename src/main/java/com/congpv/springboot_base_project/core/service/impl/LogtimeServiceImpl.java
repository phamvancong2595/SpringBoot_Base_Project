package com.congpv.springboot_base_project.core.service.impl;

import com.congpv.springboot_base_project.core.entity.Logtime;
import com.congpv.springboot_base_project.core.entity.Task;
import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.core.service.LogtimeService;
import com.congpv.springboot_base_project.core.service.TaskService;
import com.congpv.springboot_base_project.core.service.UserService;
import com.congpv.springboot_base_project.infrastructure.repository.LogtimeRepository;
import com.congpv.springboot_base_project.shared.dto.*;
import com.congpv.springboot_base_project.shared.util.ApplicationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LogtimeServiceImpl implements LogtimeService {
    private final LogtimeRepository logtimeRepository;
    private final UserService userService;
    private final TaskService taskService;

    @Override
    @Transactional
    public LogTimeResponseDto createLogtime(LogTimeRequestDto request) {
        String loginUserName = ApplicationUtil.getLoggedInUser();
        User userLogin = userService.getUserByName(loginUserName);
        Task task = taskService.findTaskById(request.taskId());
        Logtime logtime = Logtime
                .builder()
                .user(userLogin)
                .logDate(request.localDate())
                .task(task)
                .hoursSpent(request.hourSpent())
                .description(request.description())
                .build();
        Logtime savedLogtime = logtimeRepository.save(logtime);
        return mapToDto(savedLogtime);
    }

    private LogTimeResponseDto mapToDto(Logtime logtime) {
        return LogTimeResponseDto
                .builder()
                .id(logtime.getId())
                .createdBy(ApplicationUtil.getLoggedInUser())
                .taskId(logtime.getTask().getId())
                .userId(logtime.getUser().getId())
                .logDate(logtime.getLogDate())
                .hoursSpent(logtime.getHoursSpent())
                .description(logtime.getDescription())
                .build();
    }
}
