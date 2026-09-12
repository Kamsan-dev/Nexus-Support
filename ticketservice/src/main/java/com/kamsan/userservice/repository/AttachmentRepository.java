package com.kamsan.userservice.repository;

import com.kamsan.userservice.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    @Query(value = """
            SELECT *, (created_at + INTERVAL '24 HOURS') < NOW() as is_expired
            FROM account_token
            WHERE token = :token
            """, nativeQuery = true)
    Optional<AccountToken> findByToken(String token);

    void deleteByToken(String token);
}
