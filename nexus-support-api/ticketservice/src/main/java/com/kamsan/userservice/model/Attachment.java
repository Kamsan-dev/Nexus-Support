package com.kamsan.userservice.model;

import com.kamsan.userservice.sharedkernel.domain.AbstractAuditingEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "files")
public class Attachment extends AbstractAuditingEntity<Long> {

    @Id
    private Long fileId;
    private UUID filePublicId;
    private Long ticketId;
    private String extension;
    private String formattedSize;
    private String name;
    private Long size;
    private String uri;

    @Override
    public Long getId() {
        return this.fileId;
    }
}