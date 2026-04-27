package com.congpv.springboot_base_project.shared.util;

import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.infrastructure.security.CustomUserDetails;
import com.congpv.springboot_base_project.shared.constant.ApplicationConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class ApplicationUtil {
    public static String getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                Objects.equals(authentication.getPrincipal(), "anonymousUser")) {
            return ApplicationConstants.SYSTEM;
        }
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof User user) {
            username = user.getUsername();
        } else if (principal instanceof CustomUserDetails){
            username = ((CustomUserDetails) principal).getUsername(); // fallback
        } else {
            username = principal != null ? principal.toString(): "";
        }
        return username;
    }
}
