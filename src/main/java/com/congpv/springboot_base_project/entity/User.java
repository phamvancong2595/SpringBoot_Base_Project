package com.congpv.springboot_base_project.entity;

import com.congpv.springboot_base_project.enums.UserRole;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, length = 50)
    private String username;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String email;

    @NotBlank
    @Size(min = 6, max = 255)
    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    @Builder.Default
    private UserRole role = UserRole.ROLE_USER;
}
