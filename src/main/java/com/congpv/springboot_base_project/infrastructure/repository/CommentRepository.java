package com.congpv.springboot_base_project.infrastructure.repository;

import com.congpv.springboot_base_project.core.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
