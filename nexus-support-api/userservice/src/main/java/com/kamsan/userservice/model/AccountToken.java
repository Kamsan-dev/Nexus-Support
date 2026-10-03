package com.kamsan.userservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account_tokens")
public class AccountToken {
    @Id
    private Long accountTokenId;
    private Long userId;
    private String token;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public boolean isExpired(){
        return this.createdAt.plusMinutes(15).isBefore(OffsetDateTime.now());
    }
}
