package com.kamsan.userservice.service.implementation;

import com.kamsan.userservice.dto.*;
import com.kamsan.userservice.enumeration.Role;
import com.kamsan.userservice.enumeration.TicketStatus;
import com.kamsan.userservice.event.Event;
import com.kamsan.userservice.infrastructure.config.TicketProperties;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.kamsan.userservice.enumeration.EventType.*;
import static com.kamsan.userservice.utils.DateFormatter.shortDate;
import static com.kamsan.userservice.utils.TicketUtils.getFileUri;
import static com.kamsan.userservice.utils.UserUtils.hasElevatedPermissions;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;
import static org.springframework.util.StringUtils.cleanPath;

@Service
@AllArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketQueryRepository ticketQueryRepository;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final TicketMapper ticketMapper;
    private final TicketProperties ticketProperties;

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
        return ticketQueryRepository.findByUserPublicIdAndTicketPublicId(userPublicId, ticketPublicId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getTicketComments(UUID ticketPublicId) {
        return ticketQueryRepository.getCommentsForTicket(ticketPublicId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTicketTasks(UUID ticketPublicId) {
        return ticketQueryRepository.getTasksForTicket(ticketPublicId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttachmentDTO> getTicketFiles(UUID ticketPublicId) {
        List<Attachment> filesForTicket = ticketQueryRepository.getFilesForTicket(ticketPublicId);
        return filesForTicket.stream().map(ticketMapper::attachmentToAttachmentDTO).toList();
    }

    @Override
    public UUID createComment(UUID userPublicId, CreateCommentDTO createCommentDTO) {
        TicketDetailsDTO ticket = ticketQueryRepository.findByUserPublicIdAndTicketPublicId(userPublicId,
                createCommentDTO.ticketPublicId());
        ReadUserDTO user = userService.getUserByUUID(userPublicId);
        // Only an elevated user or the issuer of the given ticket can post a comment
        if (!hasElevatedPermissions(user.role()) || !userPublicId.equals(ticket.issuerPublicId())) {
            throw new ApiException("Insufficient permissions.");
        }
        UUID commentPublicId = ticketQueryRepository.insertNewComment(userPublicId, createCommentDTO);
        if (!ticket.issuerPublicId().equals(userPublicId)) {
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
        int update = ticketQueryRepository.deleteFile(userPublicId, filePublicId);
        if (update == 0) throw new ApiException("File not found or not authorized");
    }

    @Override
    public Path downloadFile(UUID filePublicId) {
        try {
            var attachment = ticketQueryRepository.findFileByPublicId(filePublicId);
            var filePath = Paths.get(ticketProperties.filesDirectory())
                                .toAbsolutePath()
                                .normalize()
                                .resolve(attachment.getName());
            if (!Files.exists(filePath))
                throw new ApiException(String.format("File %s not found on the server.", attachment.getName()));
            return filePath;
        } catch (Exception e) {
            throw new ApiException("Unable to download this file. Please try again later.");
        }
    }

    @Override
    public List<AttachmentDTO> uploadFiles(UUID userPublicId, UUID ticketPublicId, List<MultipartFile> files) {
        try {
            var fileList = new ArrayList<AttachmentDTO>();
            if (files != null && !files.isEmpty()) {
                var user = userService.getUserByUUID(userPublicId);
                TicketDetailsDTO ticket = ticketQueryRepository.findByTicketPublicId(ticketPublicId);
                if (!hasElevatedPermissions(user.role()) || !ticket.issuerPublicId().equals(userPublicId)) {
                    throw new ApiException("Insufficient permissions.");
                }
                for (MultipartFile file : files) {
                    AttachmentDTO attachment = new AttachmentDTO(
                            ticket.ticketPublicId(),
                            file.getOriginalFilename(),
                            file.getSize(),
                            byteCountToDisplaySize(file.getSize()),
                            getExtension(file.getOriginalFilename()),
                            getFileUri(file.getOriginalFilename()));

                    saveTicketFile(ticket.ticketPublicId(), attachment);
                    var filename = cleanPath(file.getOriginalFilename());
                    log.info("filename as clean path : {}", filename);
                    var fileStorageLocation = Paths.get(ticketProperties.filesDirectory(), filename)
                                                   .toAbsolutePath()
                                                   .normalize();
                    log.info("file storage URI : {}", fileStorageLocation);
                    Files.copy(file.getInputStream(), fileStorageLocation, REPLACE_EXISTING);
                    fileList.add(attachment);
                }

                if (!ticket.issuerPublicId().equals(userPublicId)) {
                    var filenames = fileList.stream()
                                            .map(AttachmentDTO::name)
                                            .collect(Collectors.joining(", "));
                    publisher.publishEvent(new Event(FILE_UPLOADED, Map.of(
                            "date", shortDate(ticket.updatedAt()),
                            "priority", ticket.priority(),
                            "ticketTitle", ticket.title(),
                            "files", filenames,
                            "name", capitalizeFully(user.firstName()),
                            "ticketNumber", ticket.ticketPublicId(),
                            "email", user.email()
                    )
                    ));
                }
            }
            return fileList;
        } catch (Exception e) {
            throw new ApiException("An error occurred. Unable to upload files.");
        }
    }

    @Override
    public void updateAssignee(UUID userPublicId, UUID assigneePublicId, UUID ticketPublicId) {
        int update = ticketQueryRepository.updateAssigneeForTicket(assigneePublicId, ticketPublicId);
        if (update == 0) throw new ApiException("Assignee or Ticket not found.");
    }

    @Override
    public UUID createTask(UUID userPublicId, CreateTaskDTO createTaskDTO) {
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
    @Transactional(readOnly = true)
    public ReadUserDTO getTicketUser(UUID ticketPublicId) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketReportDTO> report(UUID userPublicId, CreateReportDTO createReportDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isRegularUser = authentication.getAuthorities()
                                              .stream()
                                              .map(GrantedAuthority::getAuthority)
                                              .anyMatch(a -> a.equals("USER"));
        return isRegularUser ? ticketQueryRepository.generateReportForUser(userPublicId, createReportDTO)
                : ticketQueryRepository.generateReport(createReportDTO);
    }

    @Override
    public void exportPdf(HttpServletResponse response, UUID userPublicId, CreateReportDTO createReportDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isRegularUser = authentication.getAuthorities()
                                              .stream()
                                              .map(GrantedAuthority::getAuthority)
                                              .anyMatch(a -> a.equals("USER"));
        List<TicketReportDTO> ticketReportDTO = isRegularUser
                ? ticketQueryRepository.generateReportForUser(userPublicId, createReportDTO)
                : ticketQueryRepository.generateReport(createReportDTO);

    }
}
