package com.congpv.springboot_base_project.core.service.impl;

import com.congpv.springboot_base_project.core.entity.Role;
import com.congpv.springboot_base_project.core.service.RoleService;
import com.congpv.springboot_base_project.shared.dto.user.UserRequestDto;
import com.congpv.springboot_base_project.shared.dto.user.UserResponseDto;
import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.shared.exception.BadRequestException;
import com.congpv.springboot_base_project.shared.exception.ResourceNotFoundException;
import com.congpv.springboot_base_project.core.repository.UserRepository;
import com.congpv.springboot_base_project.core.service.UserService;
import com.congpv.springboot_base_project.shared.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserResponseDto createUser(UserRequestDto request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username already exists: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists: " + request.email());
        }
        Set<Role> userRoles = roleService.getRolesByCodes(request.roles());
        if (userRoles.size() != request.roles().size()) {
            throw new BadRequestException("One or more roles provided are invalid.");
        }
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .roles(userRoles)
                .build();

        User savedUser = userRepository.save(user);
        return userMapper.mapToDto(savedUser);
    }

    @Override
    @Cacheable(value = "user_detail_id", key = "#id")
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.mapToDto(user);
    }

    @Override
    @Cacheable(value = "user_detail_username", key = "#username")
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return userMapper.mapToDto(user);
    }

    @Override
    @Cacheable(value = "users")
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "user_detail_id", key = "#id"),
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user_detail_username", key = "#request.username()")
    })
    public UserResponseDto updateUser(Long id, UserRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Check unique constraints for other users
        userRepository.findByUsername(request.username())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        throw new BadRequestException("Username already exists: " + request.username());
                    }
                });

        userRepository.findByEmail(request.email())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        throw new BadRequestException("Email already exists: " + request.email());
                    }
                });

        user.setUsername(request.username() != null ? request.username() : user.getUsername());
        user.setEmail(request.email() != null ? request.email() : user.getEmail());
        user.setPassword(request.password() != null ? passwordEncoder.encode(request.password()) : user.getPassword());
        user.setFullName(request.fullName() != null ? request.fullName() : user.getFullName());
        User updatedUser = userRepository.save(user);
        return userMapper.mapToDto(updatedUser);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "user_detail_id", key = "#id"),
            @CacheEvict(value = "users", allEntries = true),
    })
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User", "name", name));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

}
