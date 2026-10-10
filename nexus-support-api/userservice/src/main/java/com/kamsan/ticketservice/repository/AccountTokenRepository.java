package com.kamsan.ticketservice.repository;

import com.kamsan.ticketservice.model.AccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTokenRepository extends JpaRepository<AccountToken, Long> {

    @Query(value = """
            SELECT *
            FROM account_tokens
            WHERE token = :token
            """, nativeQuery = true)
    Optional<AccountToken> findByToken(String token);

    void deleteByToken(String token);
}
