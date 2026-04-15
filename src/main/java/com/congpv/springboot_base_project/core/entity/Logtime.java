package com.congpv.springboot_base_project.core.entity;

import java.time.LocalDate;
import java.util.Date;

import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "log_times")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE logtimes SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Logtime extends BaseEntity {

    @Column(name = "hours_spent", nullable = false)
    private Double hoursSpent;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
