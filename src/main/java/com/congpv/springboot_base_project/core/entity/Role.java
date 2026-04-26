package com.congpv.springboot_base_project.core.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(length = 255)
    private String description;
}
