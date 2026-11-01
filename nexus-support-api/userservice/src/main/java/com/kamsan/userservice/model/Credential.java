package com.kamsan.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "credentials")
@EntityListeners(AuditingEntityListener.class)
public class Credential {
    @Id
    private Long credentialId;
    private UUID credentialPublicId;
    private Long userId;
    private String password;
    @Transient
    private boolean isExpired;

    @CreatedDate
    @Column(updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    private OffsetDateTime updatedAt;
}
