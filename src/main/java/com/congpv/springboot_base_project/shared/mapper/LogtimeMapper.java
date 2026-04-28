package com.congpv.springboot_base_project.shared.mapper;

import com.congpv.springboot_base_project.core.entity.Logtime;
import com.congpv.springboot_base_project.shared.dto.logtime.LogTimeResponseDto;
import com.congpv.springboot_base_project.shared.util.ApplicationUtil;
import org.springframework.stereotype.Component;

@Component
public class LogtimeMapper {
    public LogTimeResponseDto mapToDto(Logtime logtime) {
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
