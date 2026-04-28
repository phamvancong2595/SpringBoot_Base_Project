package com.congpv.springboot_base_project.core.service.impl;

import com.congpv.springboot_base_project.core.entity.Logtime;
import com.congpv.springboot_base_project.core.entity.Task;
import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.core.service.LogtimeService;
import com.congpv.springboot_base_project.core.service.TaskService;
import com.congpv.springboot_base_project.core.service.UserService;
import com.congpv.springboot_base_project.core.repository.LogtimeRepository;
import com.congpv.springboot_base_project.shared.dto.logtime.LogTimeRequestDto;
import com.congpv.springboot_base_project.shared.dto.logtime.LogTimeResponseDto;
import com.congpv.springboot_base_project.shared.mapper.LogtimeMapper;
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
    private final LogtimeMapper logtimeMapper;

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
        return logtimeMapper.mapToDto(savedLogtime);
    }

}
