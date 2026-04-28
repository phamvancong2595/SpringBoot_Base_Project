package com.congpv.springboot_base_project.shared.dto.login;

public record JwtAuthResponse(String accessToken, String refreshToken, String tokenType) {
    public JwtAuthResponse(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer");
    }
}
