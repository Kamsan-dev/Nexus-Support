package com.kamsan.userservice.repository;

import com.kamsan.userservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = """
            SELECT *, (created_at + INTERVAL '24 HOURS') < NOW() as is_expired
            FROM password_token
            WHERE token = :token
            """, nativeQuery = true)
    Optional<PasswordToken> findByToken(String token);

    Optional<PasswordToken> findByUserId(Long userId);

    void deleteByToken(String token);
}
