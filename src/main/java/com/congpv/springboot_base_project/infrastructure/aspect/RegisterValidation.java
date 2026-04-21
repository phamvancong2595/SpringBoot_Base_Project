package com.congpv.springboot_base_project.infrastructure.aspect;

import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.infrastructure.repository.UserRepository;
import com.congpv.springboot_base_project.shared.dto.UserRequestDto;
import com.congpv.springboot_base_project.shared.exception.RegistrationValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterValidation {
    private final CompromisedPasswordChecker compromisedPasswordChecker;
    private final UserRepository userRepository;

    @Before("""
            execution(* com.congpv.springboot_base_project.application.controller.UserController
            .createUser(..))
            """)
    public void validateBeforeRegister(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        UserRequestDto request = (UserRequestDto) args[0];
        log.info("🔍 Validating user registration request");
        Map<String, String> errors = new HashMap<>();
        // 1️⃣ Compromised password check
        CompromisedPasswordDecision decision =
                compromisedPasswordChecker.check(request.password());
        if (decision.isCompromised()) {
            errors.put("password", "Choose a strong password");
        }
        // 2️⃣ Existing user check
        Optional<User> existingUser =
                userRepository.findByUsername(request.username());

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (user.getEmail().equalsIgnoreCase(request.email())) {
                errors.put("email", "Email is already registered");
            }
        }

        // 3️⃣ Stop execution if validation fails
        if (!errors.isEmpty()) {
            log.warn("❌ Registration validation failed: {}", errors);
            throw new RegistrationValidationException(errors);
        }

        log.info("✅ Registration validation passed");
    }

}
