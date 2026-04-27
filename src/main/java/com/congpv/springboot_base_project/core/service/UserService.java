package com.congpv.springboot_base_project.core.service;

import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.shared.dto.UserRequestDto;
import com.congpv.springboot_base_project.shared.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto request);

    UserResponseDto getUserById(Long id);

    UserResponseDto getUserByUsername(String username);

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(Long id, UserRequestDto request);

    void deleteUser(Long id);

    User getUserByName(String name);
}
