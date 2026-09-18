package com.kamsan.userservice.dto;

import java.util.List;

public record TicketDTO(TicketDetailsDTO ticket,
                        List<CommentDTO> comments,
                        List<AttachmentDTO> attachments,
                        List<TaskDTO> tasks,
                        List<TicketUserDTO> techSupports,
                        TicketUserDTO assignee,
                        ReadUserDTO connectedUser) {
}
