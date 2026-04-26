package com.congpv.springboot_base_project.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "task_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskStatus extends BaseEntity{
    @NotBlank
    @Column(name = "code", nullable = false, length = 20)
    String code;

    @NotBlank
    @Column(name = "description", length = 500)
    String description;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

}
