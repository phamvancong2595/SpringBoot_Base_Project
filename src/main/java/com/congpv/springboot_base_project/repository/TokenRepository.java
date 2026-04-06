package com.congpv.springboot_base_project.repository;

import com.congpv.springboot_base_project.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
            select t from Token t inner join User u\s
            on t.user.id = u.id\s
            where u.id = :userId and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(Long userId);

    // Tìm token theo chuỗi JWT
    Optional<Token> findByToken(String token);
}
