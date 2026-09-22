package com.kamsan.userservice.service;

import com.kamsan.userservice.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public interface TicketService {

    Page<PageTicketDTO> getTickets(UUID userPublicId, PageTicketRequestDTO request);

    UUID createTicket(UUID userPublicId, CreateTicketDTO createTicketDTO, List<MultipartFile> files);

    TicketDetailsDTO getUserTicket(UUID userPublicId, UUID ticketPublicId);

    List<CommentDTO> getTicketComments(UUID ticketPublicId);

    List<TaskDTO> getTicketTasks(UUID ticketPublicId);

    UUID createComment(UUID userPublicId, CreateCommentDTO createCommentDTO);

    void updateComment(UUID userPublicID, UpdateCommentDTO updateCommentDTO);

    void deleteComment(UUID userPublicId, UUID commentPublicId);

    List<AttachmentDTO> getTicketFiles(UUID ticketPublicId);

    void deleteFile(UUID userPublicId, UUID filePublicId);

    Path downloadFile(UUID filePublicId);

    List<AttachmentDTO> uploadFiles(UUID userPublicId, UUID ticketPublicId, List<MultipartFile> files);

    void updateTicket(UUID userPublicId, UpdateTicketDTO updateTicketDTO);

    void updateAssignee(UUID userPublicId, UUID assigneePublicId, UUID ticketPublicId);

    UUID createTask(UUID userPublicId, CreateTaskDTO createTaskDTO);

    void updateTask(UUID userPublicId, UpdateTaskDTO updateTaskDTO);

    void deleteTask(UUID userPublicId, UUID taskPublicId);

    ReadUserDTO getTicketUser(UUID ticketPublicId);

    List<TicketReportDTO> report(UUID userPublicId, CreateReportDTO createReportDTO);

    void exportPdf(HttpServletResponse response, UUID userPublicId, CreateReportDTO createReportDTO);
}
