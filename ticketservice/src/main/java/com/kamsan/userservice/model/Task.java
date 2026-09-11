package com.kamsan.userservice.model;

import com.kamsan.userservice.sharedkernel.domain.AbstractAuditingEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task extends AbstractAuditingEntity<Long> {

    @Id
    private Long taskId;
    private UUID taskPublicId;
    private Long ticketIdId;
    private Long assigneeId;
    private String name;
    private String description;
    private OffsetDateTime dueDate;
    private Long statusId;

    @Override
    public Long getId() {
        return this.taskId;
    }
}