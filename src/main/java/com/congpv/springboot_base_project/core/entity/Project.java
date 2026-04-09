package com.congpv.springboot_base_project.core.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE projects SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
public class Project extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    private String description;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
