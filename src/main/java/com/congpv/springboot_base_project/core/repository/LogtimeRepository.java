package com.congpv.springboot_base_project.core.repository;

import com.congpv.springboot_base_project.core.entity.Logtime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogtimeRepository extends JpaRepository<Logtime, Long> {

}
