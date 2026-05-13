package com.congpv.springboot_base_project.core.service.impl;


import com.congpv.springboot_base_project.core.entity.Role;
import com.congpv.springboot_base_project.core.entity.Task;
import com.congpv.springboot_base_project.core.entity.TaskStatus;
import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.core.repository.TaskRepository;
import com.congpv.springboot_base_project.core.service.TaskStatusService;
import com.congpv.springboot_base_project.core.service.UserService;
import com.congpv.springboot_base_project.shared.dto.task.TaskRequestDto;
import com.congpv.springboot_base_project.shared.dto.task.TaskResponseDto;
import com.congpv.springboot_base_project.shared.exception.ResourceNotFoundException;
import com.congpv.springboot_base_project.shared.mapper.TaskMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.time.LocalDate;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskStatusService taskStatusService;

    @Mock
    private UserService userService;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private TaskRequestDto mockRequest;
    private Task mockTask;
    private TaskStatus mockStatus;
    private User mockAssignee;

    @BeforeEach
    void setUp() {
        mockRequest = new TaskRequestDto(
                "New Title", "New Desc", 2L, 15L, LocalDateTime.now(), LocalDateTime.now().plusDays(2), 5, "HIGH"
        );

        mockTask = new Task();
        mockTask.setId(1L);
        mockTask.setTitle("Old Title");

        mockStatus = new TaskStatus("IN_PROGRESS", "InProgress", false);
        mockAssignee = new User("congpv", "cong@gmail.com","congpv1234@", "Pham Van Cong", true, Set.of(new Role("USER", "user")));
    }


    @Test
    void updateTask_ShouldReturnDto_WhenDataIsValid() {
        // 1. ARRANGE
        Long projectId = 10L;
        Long taskId = 1L;
        TaskResponseDto expectedResponse = new TaskResponseDto(
                1L,
                "New Title",
                "New Desc",
                "IN_PROGRESS",
                10L,
                "HIGH",
                "congpv",
                "assignee_user",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                5,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );

        when(taskStatusService.findById(mockRequest.statusId())).thenReturn(mockStatus);
        when(taskRepository.findByIdAndProjectIdAndIsDeletedFalse(taskId, projectId))
                .thenReturn(Optional.of(mockTask));
        when(userService.findById(mockRequest.assigneeId())).thenReturn(mockAssignee);
        when(taskMapper.mapToDto(mockTask)).thenReturn(expectedResponse);

        // 2. ACT
        TaskResponseDto result = taskService.updateTask(projectId, taskId, mockRequest);

        // 3. ASSERT & VERIFY
        assertNotNull(result);
        assertEquals("New Title", result.title());

        verify(taskStatusService, times(1)).findById(mockRequest.statusId());
        verify(taskRepository, times(1)).findByIdAndProjectIdAndIsDeletedFalse(taskId, projectId);
        verify(userService, times(1)).findById(mockRequest.assigneeId());

    }


    @Test
    void updateTask_ShouldThrowException_WhenTaskNotFound() {
        // 1. ARRANGE
        Long projectId = 10L;
        Long invalidTaskId = 999L;

        when(taskStatusService.findById(mockRequest.statusId())).thenReturn(mockStatus);

        when(taskRepository.findByIdAndProjectIdAndIsDeletedFalse(invalidTaskId, projectId))
                .thenReturn(Optional.empty());

        // 2 & 3. ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.updateTask(projectId, invalidTaskId, mockRequest)
        );

        assertTrue(exception.getMessage().contains("Task"));

        verify(userService, never()).findById(anyLong());
    }


    @Test
    void updateTask_ShouldThrowException_WhenAssigneeNotFound() {
        // 1. ARRANGE
        Long projectId = 10L;
        Long taskId = 1L;

        when(taskStatusService.findById(mockRequest.statusId())).thenReturn(mockStatus);
        when(taskRepository.findByIdAndProjectIdAndIsDeletedFalse(taskId, projectId))
                .thenReturn(Optional.of(mockTask));

        when(userService.findById(mockRequest.assigneeId()))
                .thenThrow(new ResourceNotFoundException("User", "id", mockRequest.assigneeId()));

        // 2 & 3. ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.updateTask(projectId, taskId, mockRequest)
        );

        assertTrue(exception.getMessage().contains("User"));

        verify(taskMapper, never()).mapToDto(any());
    }
}
