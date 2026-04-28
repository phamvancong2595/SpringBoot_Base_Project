package com.congpv.springboot_base_project.shared.mapper;

import com.congpv.springboot_base_project.core.entity.Role;
import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.shared.dto.user.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDto mapToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .active(user.getActive())
                .roles(user.getRoles().stream().map(Role::getCode).toList())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
