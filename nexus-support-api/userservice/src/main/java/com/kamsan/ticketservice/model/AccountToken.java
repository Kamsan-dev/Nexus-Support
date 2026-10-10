package com.kamsan.ticketservice.model;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountTokenId;
    private Long userId;
    private String token;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public boolean isExpired(){
        return this.createdAt.plusMinutes(15).isBefore(OffsetDateTime.now());
    }
}
