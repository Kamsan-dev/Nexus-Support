package com.kamsan.userservice.repository;

import com.kamsan.userservice.dto.*;
import com.kamsan.userservice.enumeration.TicketStatus;
import com.kamsan.userservice.model.Ticket;
import com.kamsan.userservice.utils.TicketUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

import static com.kamsan.userservice.repository.query.TicketQuery.*;

@Service
@RequiredArgsConstructor
public class TicketQueryRepository {

    private final JdbcClient jdbc;

    public List<PageTicketDTO> getTicketsPage(UUID userPublicId, PageTicketRequestDTO request) {
        return jdbc.sql(SELECT_TICKETS_BY_ISSUER_PUBLIC_ID_QUERY)
                   .param("size", request.page().getPageSize())
                   .param("offset", request.page().getOffset())
                   .param("status", request.status())
                   .param("type", request.type())
                   .param("filter", request.filter())
                   .param("userPublicId", userPublicId)
                   .query((rs, rowNum) -> new PageTicketDTO(
                           rs.getObject("created_at", OffsetDateTime.class),
                           rs.getObject("updated_at", OffsetDateTime.class),
                           rs.getObject("ticket_public_id", UUID.class),
                           rs.getString("title"),
                           rs.getString("description"),
                           rs.getInt("progress"),
                           rs.getString("status"),
                           rs.getString("priority"),
                           rs.getString("type"),
                           rs.getObject("due_date", OffsetDateTime.class),
                           rs.getInt("file_count"),
                           rs.getInt("comment_count")
                   ))
                   .list();
    }

    public int getNumberOfTickets(UUID userPublicId, PageTicketRequestDTO request) {
        return jdbc.sql(SELECT_COUNT_TICKET_NUMBER_QUERY)
                   .param("userPublicId", userPublicId)
                   .query(Integer.class)
                   .single();
    }

    public Ticket insertNewTicket(UUID userPublicId, CreateTicketDTO request, TicketStatus status) {
        return jdbc.sql(INSERT_TICKET_QUERY)
                   .param("ticketPublicId", TicketUtils.randomUUID.get())
                   .param("description", request.description())
                   .param("title", request.title())
                   .param("type", request.type())
                   .param("priority", request.priority())
                   .param("status", status.value())
                   .param("userPublicId", userPublicId)
                   .query(Ticket.class)
                   .single();
    }

    public TicketDTO getTicketByTicketPublicId(UUID userPublicId, UUID ticketPublicId) {
        return jdbc.sql(SELECT_TICKET_BY_USER_AND_TICKET_PUBLIC_ID)
                   .param("ticketPublicId", ticketPublicId)
                   .param("userPublicId", userPublicId)
                   .query((rs, rowNum) -> new TicketDTO(
                           rs.getObject("created_at", OffsetDateTime.class),
                           rs.getObject("updated_at", OffsetDateTime.class),
                           rs.getObject("ticket_public_id", UUID.class),
                           userPublicId,
                           rs.getObject("assignee_public_id", UUID.class),
                           rs.getString("title"),
                           rs.getString("description"),
                           rs.getInt("progress"),
                           rs.getString("status"),
                           rs.getString("priority"),
                           rs.getString("type"),
                           rs.getObject("due_date", OffsetDateTime.class)
                   ))
                   .single();
    }

    public List<CommentDTO> getCommentsByTicketPublicId(UUID ticketPublicId) {
        return jdbc.sql(SELECT_COMMENTS_BY_TICKET_PUBLIC_ID)
                   .param("ticketPublicId", ticketPublicId)
                   .query((rs, rowNum) -> new CommentDTO(
                           rs.getObject("created_at", OffsetDateTime.class),
                           rs.getObject("updated_at", OffsetDateTime.class),
                           rs.getObject("comment_public_id", UUID.class),
                           rs.getObject("user_public_id", UUID.class),
                           ticketPublicId,
                           rs.getString("comment"),
                           rs.getBoolean("is_edited"),
                           rs.getString("first_name"),
                           rs.getString("last_name"),
                           rs.getString("image_url")
                   ))
                   .list();
    }

//    OffsetDateTime createdAt,
//    OffsetDateTime updatedAt,
//    UUID ticketPublicId,
//    Long issuerPublicId,
//    Long assigneePublicId,
//    String title,
//    String description,
//    int progress,
//    String status,
//    String priority,
//    String typeId,
//    OffsetDateTime dueDate

    private final BiFunction<Integer, Integer, Integer> getOffset = (page, size) -> page * size;

}
