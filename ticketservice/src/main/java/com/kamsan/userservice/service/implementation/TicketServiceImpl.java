package com.kamsan.userservice.service.implementation;

import com.kamsan.userservice.dto.*;
import com.kamsan.userservice.enumeration.TicketStatus;
import com.kamsan.userservice.mapper.TicketMapper;
import com.kamsan.userservice.model.Ticket;
import com.kamsan.userservice.repository.TicketQueryRepository;
import com.kamsan.userservice.service.TicketService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketQueryRepository ticketQueryRepository;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<PageTicketDTO> getTickets(UUID userPublicId, PageTicketRequestDTO request) {
        List<PageTicketDTO> ticketsPage = ticketQueryRepository.getTicketsPage(userPublicId, request);
        int totalElements = ticketQueryRepository.getNumberOfTickets(userPublicId, request);
        return new PageImpl<>(ticketsPage, request.page(), totalElements);
    }

    @Override
    @Transactional
    public TicketDTO createTicket(UUID userPublicId, CreateTicketDTO createTicketDTO) {
        Ticket ticket = ticketQueryRepository.insertNewTicket(userPublicId, createTicketDTO, TicketStatus.NEW);
        return ticketMapper.ticketToTicketDTO(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketDTO getUserTicket(UUID userPublicId, UUID ticketPublicId) {
        return ticketQueryRepository.getTicketByTicketPublicId(userPublicId, ticketPublicId);
    }

    @Override
    public List<CommentDTO> getTicketComments(UUID ticketPublicId) {
        return ticketQueryRepository.getCommentsByTicketPublicId(ticketPublicId);
    }

    @Override
    public List<TaskDTO> getTicketTasks(UUID ticketPublicId) {
        return List.of();
    }

    @Override
    public CommentDTO createComment(UUID userPublicId, CreateCommentDTO createCommentDTO) {
        return null;
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
        return List.of();
    }

    @Override
    public List<AttachmentDTO> uploadFiles(UUID userPublicId, UploadTicketFilesDTO uploadTicketFilesDTO) {
        return List.of();
    }

    @Override
    public void deleteFile(UUID userPublicId, UUID filePublicId) {

    }

    @Override
    public Path downloadFile(String name, UUID filePublicId) {
        return null;
    }

    @Override
    public TicketDTO updateTicket(UUID userPublicId, UpdateTicketDTO updateTicketDTO) {
        return null;
    }

    @Override
    public ReadUserDTO getAssignee(UUID ticketPublicId) {
        return null;
    }

    @Override
    public List<ReadUserDTO> getTechSupports() {
        return List.of();
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
    public List<TicketDTO> report(UUID userPublicId, CreateReportDTO createReportDTO) {
        return List.of();
    }

    @Override
    public void exportPdf(HttpServletResponse response, UUID userPublicId, CreateReportDTO createReportDTO) {

    }
}
