package com.kamsan.userservice.repository;

import com.kamsan.userservice.dto.*;
import com.kamsan.userservice.enumeration.TicketPriority;
import com.kamsan.userservice.enumeration.TicketStatus;
import com.kamsan.userservice.enumeration.TicketType;
import com.kamsan.userservice.model.Attachment;
import com.kamsan.userservice.utils.TicketUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

import static com.kamsan.userservice.repository.query.TicketQuery.*;
import static com.kamsan.userservice.utils.QueryUtils.*;

@Service
@RequiredArgsConstructor
public class TicketQueryRepository {

    private final JdbcClient jdbc;

    public List<PageTicketDTO> getTickets(PageTicketRequestDTO request) {
        var query = createSelectTicketsQuery(request.status(), request.type(), request.filter(), null);
        return jdbc.sql(query)
                   .param("size", request.page().getPageSize())
                   .param("offset", request.page().getOffset())
                   .param("status", request.status())
                   .param("type", request.type())
                   .param("filter", request.filter())
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

    public List<PageTicketDTO> getUserTickets(UUID userPublicId, PageTicketRequestDTO request) {
        var query = createSelectTicketsQuery(request.status(), request.type(), request.filter(), userPublicId);
        return jdbc.sql(query)
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
        var query = createSelectTotalElementsQuery(request.status(), request.type(), request.filter(), userPublicId);
        return jdbc.sql(query)
                   .param("userPublicId", userPublicId)
                   .query(Integer.class)
                   .single();
    }

    public UUID insertNewTicket(UUID userPublicId, CreateTicketDTO request, TicketStatus status) {
        return jdbc.sql(INSERT_TICKET_QUERY)
                   .param("ticketPublicId", TicketUtils.randomUUID.get())
                   .param("description", request.description())
                   .param("title", request.title())
                   .param("type", request.type())
                   .param("priority", request.priority())
                   .param("status", status.value())
                   .param("userPublicId", userPublicId)
                   .query(UUID.class)
                   .single();
    }

    public TicketDetailsDTO getTicket(UUID userPublicId, UUID ticketPublicId) {
        return jdbc.sql(SELECT_TICKET_BY_USER_AND_TICKET_PUBLIC_ID_QUERY)
                   .param("ticketPublicId", ticketPublicId)
                   .param("userPublicId", userPublicId)
                   .query((rs, rowNum) -> new TicketDetailsDTO(
                           rs.getObject("created_at", OffsetDateTime.class),
                           rs.getObject("updated_at", OffsetDateTime.class),
                           rs.getObject("ticket_public_id", UUID.class),
                           userPublicId,
                           rs.getObject("assignee_public_id", UUID.class),
                           rs.getString("title"),
                           rs.getString("description"),
                           rs.getInt("progress"),
                           rs.getObject("status", TicketStatus.class),
                           rs.getObject("priority", TicketPriority.class),
                           rs.getObject("type", TicketType.class),
                           rs.getObject("due_date", OffsetDateTime.class)
                   ))
                   .single();
    }

    public List<CommentDTO> getCommentsForTicket(UUID ticketPublicId) {
        return jdbc.sql(SELECT_COMMENTS_BY_TICKET_PUBLIC_ID_QUERY)
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

    public List<TaskDTO> getTasksForTicket(UUID ticketPublicId) {
        return jdbc.sql(SELECT_TASKS_BY_TICKET_PUBLIC_ID_QUERY)
                   .param("ticketPublicId", ticketPublicId)
                   .query((rs, rowNum) -> new TaskDTO(
                           rs.getObject("task_public_id", UUID.class),
                           ticketPublicId,
                           rs.getObject("assignee_public_id", UUID.class),
                           rs.getString("name"),
                           rs.getString("description"),
                           rs.getObject("due_date", OffsetDateTime.class),
                           rs.getObject("status", TicketStatus.class),
                           rs.getString("first_name"),
                           rs.getString("last_name"),
                           rs.getString("image_url"),
                           rs.getObject("created_at", OffsetDateTime.class),
                           rs.getObject("updated_at", OffsetDateTime.class)
                   ))
                   .list();
    }

    public UUID insertNewComment(UUID userPublicId, CreateCommentDTO request) {
        return jdbc.sql(INSERT_COMMENT_TICKET_QUERY)
                   .param("ticketPublicId", request.ticketPublicId())
                   .param("userPublicId", userPublicId)
                   .param("commentPublicId", TicketUtils.randomUUID.get())
                   .param("comment", request.comment())
                   .query(UUID.class)
                   .single();
    }

    public void insertNewFile(UUID ticketPublicId, AttachmentDTO request) {
        jdbc.sql(INSERT_FILE_TICKET_QUERY)
            .param("ticketPublicId", ticketPublicId)
            .param("filePublicId", TicketUtils.randomUUID.get())
            .param("extension", request.extension())
            .param("formattedSize", request.formattedSize())
            .param("name", request.name())
            .param("size", request.size())
            .param("uri", request.uri())
            .update();
    }

    public List<Attachment> getFilesForTicket(UUID ticketPublicId) {
        return jdbc.sql(SELECT_FILES_TICKET_QUERY)
                   .param("ticketPublicId", ticketPublicId)
                   .query(Attachment.class)
                   .list();
    }

    public void deleteFile(UUID filePublicId) {
        jdbc.sql(DELETE_FILE_QUERY)
            .param("filePublicId", filePublicId)
            .update();
    }

    public void updateComment(UUID commentPublicId, String comment) {
        jdbc.sql(UPDATE_COMMENT_QUERY)
            .param("comment", comment)
            .param("commentPublicId", commentPublicId)
            .update();
    }

    public void deleteComment(UUID commentPublicId) {
        jdbc.sql(DELETE_COMMENT_QUERY)
            .param("commentPublicId", commentPublicId)
            .update();
    }

    public void updateTicket(UpdateTicketDTO request) {
        jdbc.sql(UPDATE_TICKET_QUERY)
            .param("ticketPublicId", request.ticketPublicId())
            .param("title", request.title())
            .param("description", request.description())
            .param("type", request.type())
            .param("status", request.status())
            .param("priority", request.priority())
            .param("dueDate", request.dueDate())
            .param("progress", request.progress())
            .update();
    }

    public void updateTicket(UUID assigneePublicId, UUID ticketPublicId) {
        jdbc.sql(UPDATE_ASSIGNEE_TICKET_QUERY)
            .param("ticketPublicId", ticketPublicId)
            .param("assigneePublicId", assigneePublicId)
            .update();
    }

    public TaskDTO insertNewTask(UUID userPublicId, CreateTaskDTO request) {
        return jdbc.sql(INSERT_TICKET_TASK_QUERY)
                   .param("ticketPublicId", request.ticketPublicId())
                   .param("assigneePublicId", userPublicId)
                   .param("taskPublicId", TicketUtils.randomUUID.get())
                   .param("name", request.name())
                   .param("description", request.description())
                   .param("status", request.status())
                   .query((rs, rowNum) -> new TaskDTO(
                           rs.getObject("task_public_id", UUID.class),
                           request.ticketPublicId(),
                           userPublicId,
                           rs.getString("name"),
                           rs.getString("description"),
                           rs.getObject("due_date", OffsetDateTime.class),
                           rs.getObject("status", TicketStatus.class),
                           rs.getString("first_name"),
                           rs.getString("last_name"),
                           rs.getString("image_url"),
                           rs.getObject("created_at", OffsetDateTime.class),
                           rs.getObject("updated_at", OffsetDateTime.class)
                   ))
                   .single();
    }

    public List<TicketReportDTO> generateReport(UUID userPublicId, CreateReportDTO request) {
        var query = createTicketReportQuery(request);
        return jdbc.sql(query)
                   .param("userPublicId", userPublicId)
                   .param("statuses", request.statuses())
                   .param("types", request.types())
                   .param("priorities", request.priorities())
                   .param("filter", request.filter())
                   .param("fromDate", request.fromDate())
                   .param("toDate", request.toDate())
                   .query((rs, rowNum) -> new TicketReportDTO(
                           rs.getObject("ticket_public_id", UUID.class),
                           rs.getString("title"),
                           rs.getString("description"),
                           rs.getObject("status", TicketStatus.class),
                           rs.getObject("priority", TicketPriority.class),
                           rs.getObject("type", TicketType.class),
                           rs.getObject("due_date", OffsetDateTime.class),
                           rs.getObject("created_at", OffsetDateTime.class),
                           rs.getObject("updated_at", OffsetDateTime.class)
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
