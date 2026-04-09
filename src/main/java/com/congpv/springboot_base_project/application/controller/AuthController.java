package com.congpv.springboot_base_project.application.controller;

import com.congpv.springboot_base_project.shared.dto.ApiResponse;
import com.congpv.springboot_base_project.shared.dto.JwtAuthResponse;
import com.congpv.springboot_base_project.shared.dto.LoginRequestDto;
import com.congpv.springboot_base_project.shared.dto.RefreshTokenRequest;
import com.congpv.springboot_base_project.core.entity.Token;
import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.infrastructure.repository.TokenRepository;
import com.congpv.springboot_base_project.infrastructure.repository.UserRepository;
import com.congpv.springboot_base_project.infrastructure.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String reqRefreshToken = request.getRefreshToken();

        // 1. Giải mã token để lấy username (Email)
        String username = tokenProvider.getUsernameFromJWT(reqRefreshToken);

        if (username != null) {
            // 2. Tìm User trong DB
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null));

            // 3. Kiểm tra xem Refresh Token này có hợp lệ không
            if (tokenProvider.validateToken(reqRefreshToken)) {

                // 4. KIỂM TRA TRONG BẢNG `tokens` (Bảo mật 2 lớp)
                // Đảm bảo token này chưa bị Admin khóa (revoked/expired = true)
                Token storedToken = tokenRepository.findByRefreshToken(reqRefreshToken)
                        .orElseThrow(() -> new RuntimeException("Token không tồn tại trong DB"));

                if (storedToken.isRevoked() || storedToken.isExpired()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token đã bị thu hồi!");
                }

                // 5. MỌI THỨ OK -> TẠO ACCESS TOKEN MỚI
                String newAccessToken = tokenProvider.generateAccessToken(authentication);

                // 6. Cập nhật lại Access Token mới vào bảng `tokens`
                storedToken.setAccessToken(newAccessToken);
                tokenRepository.save(storedToken);

                // 7. Trả về cho Frontend
                return ResponseEntity.ok(Map.of(
                        "accessToken", newAccessToken,
                        "refreshToken", reqRefreshToken // Vẫn giữ nguyên thẻ VIP cũ
                ));
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh Token không hợp lệ!");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> authenticateUser(
            @Valid @RequestBody LoginRequestDto loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 4. Thu hồi các token cũ đang có và Lưu token mới
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken, refreshToken);

        return ResponseEntity.ok(ApiResponse.success(new JwtAuthResponse(accessToken, refreshToken)));
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        // Lặp qua danh sách token hợp lệ và chuyển trạng thái thành đã thu hồi/hết hạn
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken, String refreshToken) {
        Token token = Token.builder()
                .user(user)
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}