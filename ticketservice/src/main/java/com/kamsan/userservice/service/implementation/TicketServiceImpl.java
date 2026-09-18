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
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public UUID createComment(UUID userPublicId, CreateCommentDTO createCommentDTO) {
        return ticketQueryRepository.insertNewComment(userPublicId, createCommentDTO);
    }

    @Override
    public CommentDTO updateComment(UUID userPublicID, UpdateCommentDTO updateCommentDTO) {
        return null;
    }

    @Override
    public void deleteComment(UUID userPublicId, UUID commentPublicId) {

    }

    @Override
    public List<AttachmentDTO> getTicketFiles(UUID ticketPublicId) {
        List<Attachment> filesForTicket = ticketQueryRepository.getFilesForTicket(ticketPublicId);
        return filesForTicket.stream().map(ticketMapper::attachmentToAttachmentDTO).toList();
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
    public TicketDetailsDTO updateTicket(UUID userPublicId, UpdateTicketDTO updateTicketDTO) {
        return null;
    }

    @Override
    public ReadUserDTO updateAssignee(UUID userPublicId, UUID assigneePublicId, UUID ticketPublicId) {
        return null;
    }

    @Override
    public TaskDTO createTask(UUID userPublicId, CreateTaskDTO createTaskDTO) {
        return null;
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
}
