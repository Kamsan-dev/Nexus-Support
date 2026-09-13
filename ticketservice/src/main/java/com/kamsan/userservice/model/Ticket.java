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

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket extends AbstractAuditingEntity<Long> {

    @Id
    private Long ticketId;
    private UUID ticketPublicId;
    private Long issuerId;
    private Long assigneeId;
    private String title;
    private String description;
    private int progress;
    private OffsetDateTime dueDate;
    private Long statusId;
    private Long priorityId;
    private Long typeId;

    @Transient
    private int fileCount;
    @Transient
    private int commentCount;
    @Transient
    private String status;
    @Transient
    private String priority;
    @Transient
    private String type;

    @Override
    public Long getId() {
        return this.ticketId;
    }
}
