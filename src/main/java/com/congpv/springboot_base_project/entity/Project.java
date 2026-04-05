package com.congpv.springboot_base_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    private String description;
}
