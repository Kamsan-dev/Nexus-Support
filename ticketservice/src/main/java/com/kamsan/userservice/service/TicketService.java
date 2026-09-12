package com.kamsan.userservice.service;

import com.kamsan.userservice.dto.*;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public interface TicketService {

    List<TicketDTO> getTickets(UUID userPublicId, PageTicketRequestDTO request);

    int getPages(int page);

    TicketDTO createTicket(UUID userPublicId, CreateTicketDTO createTicketDTO);

    TicketDTO getUserTicket(UUID userPublicId, UUID ticketPublicId);

    List<CommentDTO> getTicketComments(UUID ticketPublicId);

    List<TaskDTO> getTicketTasks(UUID ticketPublicId);

    CommentDTO createComment(UUID userPublicId, CreateCommentDTO createCommentDTO);

    CommentDTO updateComment(UUID userPublicID, UpdateCommentDTO updateCommentDTO);

    void deleteComment(UUID userPublicId, UUID commentPublicId);

    List<AttachmentDTO> getTicketFiles(UUID ticketPublicId);

    List<AttachmentDTO> uploadFiles(UUID userPublicId, UploadTicketFilesDTO uploadTicketFilesDTO);

    void deleteFile(UUID userPublicId, UUID filePublicId);

    Path downloadFile(String name, UUID filePublicId);

    TicketDTO updateTicket(UUID userPublicId, UpdateTicketDTO updateTicketDTO);

    ReadUserDTO getAssignee(UUID ticketPublicId);

    List<ReadUserDTO> getTechSupports();

    ReadUserDTO updateAssignee(UUID userPublicId, UUID assigneePublicId, UUID ticketPublicId);

    TaskDTO createTask(UUID userPublicId, CreateTaskDTO createTaskDTO);

    ReadUserDTO getTicketUser(UUID ticketPublicId);

    List<TicketDTO> report(UUID userPublicId, CreateReportDTO createReportDTO);

    void exportPdf(HttpServletResponse response, UUID userPublicId, CreateReportDTO createReportDTO);
}
