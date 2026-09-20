package com.kamsan.userservice.service.implementation;

import com.kamsan.userservice.dto.*;
import com.kamsan.userservice.enumeration.Role;
import com.kamsan.userservice.enumeration.TicketStatus;
import com.kamsan.userservice.event.Event;
import com.kamsan.userservice.mapper.TicketMapper;
import com.kamsan.userservice.model.Attachment;
import com.kamsan.userservice.repository.TicketQueryRepository;
import com.kamsan.userservice.service.TicketService;
import com.kamsan.userservice.service.UserService;
import com.kamsan.userservice.sharedkernel.exception.ApiException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.kamsan.userservice.enumeration.EventType.COMMENT_CREATED;
import static com.kamsan.userservice.enumeration.EventType.TICKET_CREATED;
import static com.kamsan.userservice.utils.TicketUtils.getFileUri;
import static com.kamsan.userservice.utils.UserUtils.hasElevatedPermissions;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

@Service
@AllArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketQueryRepository ticketQueryRepository;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<PageTicketDTO> getTickets(UUID userPublicId, PageTicketRequestDTO request) {
        ReadUserDTO connectedUser = userService.getUserByUUID(userPublicId);
        Role userRole = connectedUser.role();

        List<PageTicketDTO> ticketsPage = hasElevatedPermissions(userRole)
                ? ticketQueryRepository.getTickets(request)
                : ticketQueryRepository.getUserTickets(userPublicId, request);

        int totalElements = ticketQueryRepository.getNumberOfTickets(
                hasElevatedPermissions(userRole) ? null : userPublicId, request);

        return new PageImpl<>(ticketsPage, request.page(), totalElements);
    }

    @Override
    @Transactional
    public UUID createTicket(UUID userPublicId, CreateTicketDTO newTicket, List<MultipartFile> files) {
        UUID ticketPublicId = ticketQueryRepository.insertNewTicket(userPublicId, newTicket, TicketStatus.NEW);
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                AttachmentDTO attachment = new AttachmentDTO(ticketPublicId,
                        file.getOriginalFilename(),
                        file.getSize(),
                        byteCountToDisplaySize(file.getSize()),
                        getExtension(file.getOriginalFilename()),
                        getFileUri(file.getOriginalFilename()));

                saveTicketFile(ticketPublicId, attachment);
            }
            // Save bytes on R2
            ReadUserDTO user = userService.getUserByUUID(userPublicId);
            publisher.publishEvent(new Event(TICKET_CREATED,
                    Map.of("priority", newTicket.priority(),
                            "ticketTitle", newTicket.title(),
                            "ticketNumber", ticketPublicId,
                            "name", capitalizeFully(user.firstName()),
                            "email", user.email())));
        }
        return ticketPublicId;
    }

    @Override
    public void updateTicket(UUID userPublicId, UpdateTicketDTO updateTicketDTO) {
        if (!updateTicketDTO.issuerPublicId().equals(userPublicId)) {
            throw new ApiException("You can not update a ticket that does not belong to you.");
        }
        int update = this.ticketQueryRepository.updateTicket(userPublicId, updateTicketDTO);
        if (update == 0) throw new ApiException("Ticket not found or not authorized");
    }

    @Override
    @Transactional(readOnly = true)
    public TicketDetailsDTO getUserTicket(UUID userPublicId, UUID ticketPublicId) {
        return ticketQueryRepository.getTicket(userPublicId, ticketPublicId);
    }

    @Override
    public List<CommentDTO> getTicketComments(UUID ticketPublicId) {
        return ticketQueryRepository.getCommentsForTicket(ticketPublicId);
    }

    @Override
    public List<TaskDTO> getTicketTasks(UUID ticketPublicId) {
        return ticketQueryRepository.getTasksForTicket(ticketPublicId);
    }

    @Override
    public List<AttachmentDTO> getTicketFiles(UUID ticketPublicId) {
        List<Attachment> filesForTicket = ticketQueryRepository.getFilesForTicket(ticketPublicId);
        return filesForTicket.stream().map(ticketMapper::attachmentToAttachmentDTO).toList();
    }

    @Override
    @Transactional
    public UUID createComment(UUID userPublicId, CreateCommentDTO createCommentDTO) {
        TicketDetailsDTO ticket = ticketQueryRepository.getTicket(userPublicId, createCommentDTO.ticketPublicId());
        ReadUserDTO user = userService.getUserByUUID(userPublicId);

        // Only an elevated user or the issuer of the given ticket can post a comment
        if (!hasElevatedPermissions(user.role()) || !userPublicId.equals(ticket.issuerPublicId())) {
            throw new ApiException("Insufficient permissions.");
        }
        UUID commentPublicId = ticketQueryRepository.insertNewComment(userPublicId, createCommentDTO);
        if (!Objects.equals(userPublicId, ticket.issuerPublicId())) {
            publisher.publishEvent(new Event(COMMENT_CREATED, Map.of(
                    "date", shortDate(ticket.createdAt()),
                    "priority", ticket.priority(),
                    "ticketTitle", ticket.title(),
                    "comment", createCommentDTO.comment(),
                    "ticketNumber", ticket.ticketPublicId(),
                    "commentOwner", capitalizeFully(user.firstName()),
                    "email", user.email()
            )
            ));
        }
        return commentPublicId;
    }

    @Override
    @Transactional
    public void updateComment(UUID connectedUser, UpdateCommentDTO updateCommentDTO) {
        if (!updateCommentDTO.ownerCommentPublicId().equals(connectedUser)) {
            throw new ApiException("You can not update a comment that does not belong to you.");
        }
        int update = ticketQueryRepository.updateComment(connectedUser,
                updateCommentDTO.commentPublicId(),
                updateCommentDTO.comment());
        if (update == 0) throw new ApiException("Comment not found or not authorized");
    }

    @Override
    public void deleteComment(UUID userPublicId, UUID commentPublicId) {
        int update = ticketQueryRepository.deleteComment(userPublicId, commentPublicId);
        if (update == 0) throw new ApiException("Comment not found or not authorized");
    }

    public void saveTicketFile(UUID ticketPublicId, AttachmentDTO attachmentDTO) {
        ticketQueryRepository.insertNewFile(ticketPublicId, attachmentDTO);
    }

    @Override
    public void deleteFile(UUID userPublicId, UUID filePublicId) {

    }

    @Override
    public Path downloadFile(String name, UUID filePublicId) {
        return null;
    }

    @Override
    public void updateAssignee(UUID userPublicId, UUID assigneePublicId, UUID ticketPublicId) {
        ReadUserDTO connectedUser = userService.getUserByUUID(userPublicId);
        if (!hasElevatedPermissions(connectedUser.role())) {
            throw new ApiException("Insufficient permissions.");
        }
        int update = ticketQueryRepository.updateAssigneeForTicket(assigneePublicId, ticketPublicId);
        if (update == 0) throw new ApiException("Assignee or Ticket not found.");
    }

    @Override
    public UUID createTask(UUID userPublicId, CreateTaskDTO createTaskDTO) {
        var ticket = ticketQueryRepository.getTicket(userPublicId, createTaskDTO.ticketPublicId());
        var user = userService.getUserByUUID(userPublicId);
        if (!hasElevatedPermissions(user.role()) || !ticket.issuerPublicId().equals(userPublicId)) {
            throw new ApiException("Insufficient permissions.");
        }
        TaskDTO taskDTO = ticketQueryRepository.insertNewTask(userPublicId, createTaskDTO);
        return taskDTO.taskPublicId();
    }

    @Override
    public void updateTask(UUID userPublicId, UpdateTaskDTO updateTaskDTO) {
        if (!updateTaskDTO.assigneePublicId().equals(userPublicId)) {
            throw new ApiException("Insufficient permissions.");
        }
        int update = ticketQueryRepository.updateTask(userPublicId, updateTaskDTO);
        if (update == 0) throw new ApiException("Assignee or Task not found.");
    }

    @Override
    public void deleteTask(UUID userPublicId, UUID taskPublicId) {
        int update = ticketQueryRepository.deleteTask(userPublicId, taskPublicId);
        if (update == 0) throw new ApiException("Task not found or not authorized");
    }

    @Override
    public ReadUserDTO getTicketUser(UUID ticketPublicId) {
        return null;
    }

    @Override
    public List<TicketDetailsDTO> report(UUID userPublicId, CreateReportDTO createReportDTO) {
        return List.of();
    }

    @Override
    public void exportPdf(HttpServletResponse response, UUID userPublicId, CreateReportDTO createReportDTO) {

    }

    private Object shortDate(OffsetDateTime offsetDateTime) {
        return null;
    }
}
