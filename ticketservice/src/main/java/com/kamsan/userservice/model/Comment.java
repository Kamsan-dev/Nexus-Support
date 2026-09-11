package com.kamsan.userservice.model;

import com.kamsan.userservice.sharedkernel.domain.AbstractAuditingEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends AbstractAuditingEntity<Long> {

    @Id
    private Long commentId;
    private UUID commentPublicId;
    private Long userId;
    private Long ticketId;
    private String comment;
    private boolean isEdited;

    @Transient
    private UUID userPublicId;
    private String firstName;
    @Transient
    private String lastName;
    @Transient
    private String imageUrl;

    @Override
    public Long getId() {
        return this.commentId;
    }
}