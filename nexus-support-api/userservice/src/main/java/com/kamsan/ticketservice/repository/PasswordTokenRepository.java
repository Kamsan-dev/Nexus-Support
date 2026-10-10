package com.kamsan.ticketservice.repository;

import com.kamsan.ticketservice.model.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {

    Optional<PasswordToken> findByToken(String token);

    Optional<PasswordToken> findByUserId(Long userId);

    void deleteByToken(String token);
}
