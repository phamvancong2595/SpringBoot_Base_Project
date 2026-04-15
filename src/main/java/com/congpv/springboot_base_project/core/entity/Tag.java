package com.congpv.springboot_base_project.core.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 20)
    private String color;
    @Builder.Default
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();
}