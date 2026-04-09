package com.congpv.springboot_base_project.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true)
    public String token;

    // Trạng thái bị thu hồi (ví dụ: khi user đăng nhập ở máy khác)
    public boolean revoked;

    // Trạng thái hết hạn
    public boolean expired;

    // Khóa ngoại nối đến bảng User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}