package com.kamsan.ticketservice.dto;

import com.kamsan.ticketservice.enumeration.TicketPriority;
import com.kamsan.ticketservice.enumeration.TicketStatus;
import com.kamsan.ticketservice.enumeration.TicketType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageTicketDTO {
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private UUID ticketPublicId;
    private String title;
    private String description;
    private @Min(0) @Max(100) int progress;
    private TicketStatus status;
    private TicketPriority priority;
    private TicketType type;
    private OffsetDateTime dueDate;
    private int fileCount;
    private int commentCount;
}
