package com.kamsan.ticketservice.repository;

import com.kamsan.ticketservice.dto.*;
import com.kamsan.ticketservice.enumeration.TicketPriority;
import com.kamsan.ticketservice.enumeration.TicketStatus;
import com.kamsan.ticketservice.enumeration.TicketType;
import com.kamsan.ticketservice.model.Attachment;
import com.kamsan.ticketservice.utils.TicketUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.kamsan.ticketservice.repository.query.TicketQuery.*;
import static com.kamsan.ticketservice.utils.QueryUtils.*;

@Service
@RequiredArgsConstructor
public class TicketQueryRepository {

    private final JdbcClient jdbc;

    public List<PageTicketDTO> getTickets(PageTicketRequestDTO request) {
        var query = createSelectTicketsQuery(request.status(), request.type(), request.filter(), null);
        return jdbc.sql(query)
                   .param("size", request.page().getPageSize())
                   .param("offset", request.page().getOffset())
                   .param("status", request.status() != null
                           ? request.status().getValue() : null, Types.VARCHAR)
                   .param("type", request.type() != null
                           ? request.type().getValue() : null, Types.VARCHAR)
                   .param("filter", request.filter())
                   .query((rs, rowNum) -> new PageTicketDTO(
                           rs.getObject("created_at", OffsetDateTime.class),
                           rs.getObject("updated_at", OffsetDateTime.class),
                           rs.getObject("ticket_public_id", UUID.class),
                           rs.getString("title"),
                           rs.getString("description"),
                           rs.getInt("progress"),
                           TicketStatus.valueOf(rs.getString("status")),
                           TicketPriority.valueOf(rs.getString("priority")),
                           TicketType.valueOf(rs.getString("type")),
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
                   .param("status", request.status() != null
                           ? request.status().getValue() : null, Types.VARCHAR)
                   .param("type", request.type() != null
                           ? request.type().getValue() : null, Types.VARCHAR)
                   .param("filter", request.filter())
                   .param("userPublicId", userPublicId)
                   .query((rs, rowNum) -> new PageTicketDTO(
                           rs.getObject("created_at", OffsetDateTime.class),
                           rs.getObject("updated_at", OffsetDateTime.class),
                           rs.getObject("ticket_public_id", UUID.class),
                           rs.getString("title"),
                           rs.getString("description"),
                           rs.getInt("progress"),
                           TicketStatus.valueOf(rs.getString("status")),
                           TicketPriority.valueOf(rs.getString("priority")),
                           TicketType.valueOf(rs.getString("type")),
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
                   .param("status", request.status() != null
                           ? request.status().getValue() : null, Types.VARCHAR)
                   .param("type", request.type() != null
                           ? request.type().getValue() : null, Types.VARCHAR)
                   .param("filter", request.filter())
                   .query(Integer.class)
                   .single();
    }

    public PageTicketDTO insertNewTicket(UUID userPublicId, CreateTicketDTO request, TicketStatus status) {
        return jdbc.sql(INSERT_TICKET_QUERY)
                   .param("ticketPublicId", TicketUtils.randomUUID.get())
                   .param("description", request.description())
                   .param("title", request.title())
                   .param("type", request.type().getValue())
                   .param("priority", request.priority().getValue())
                   .param("status", status.getValue())
                   .param("userPublicId", userPublicId)
                   .query(PageTicketDTO.class)
                   .single();
    }

    public TicketDetailsDTO findByTicketPublicId(UUID ticketPublicId) {
        return jdbc.sql(SELECT_TICKET_BY_TICKET_PUBLIC_ID_QUERY)
                   .param("ticketPublicId", ticketPublicId)
                   .query((rs, rowNum) -> new TicketDetailsDTO(
                           rs.getObject("created_at", OffsetDateTime.class),
                           rs.getObject("updated_at", OffsetDateTime.class),
                           rs.getObject("ticket_public_id", UUID.class),
                           rs.getObject("issuer_public_id", UUID.class),
                           rs.getObject("assignee_public_id", UUID.class),
                           rs.getString("title"),
                           rs.getString("description"),
                           rs.getInt("progress"),
                           TicketStatus.valueOf(rs.getString("status")),
                           TicketPriority.valueOf(rs.getString("priority")),
                           TicketType.valueOf(rs.getString("type")),
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
                           TicketStatus.valueOf(rs.getString("status")),
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

    public Attachment findFileByPublicId(UUID filePublicId) {
        return jdbc.sql(SELECT_FILES_TICKET_QUERY)
                   .param("filePublicId", filePublicId)
                   .query(Attachment.class)
                   .single();
    }

    public int deleteFile(UUID userPublicId, UUID filePublicId) {
        return jdbc.sql(DELETE_FILE_QUERY)
                   .param("filePublicId", filePublicId)
                   .param("userPublicId", userPublicId)
                   .update();
    }

    public int updateComment(UUID userPublicId, UUID commentPublicId, String comment) {
        return jdbc.sql(UPDATE_COMMENT_QUERY)
                   .param("commentPublicId", commentPublicId)
                   .param("userPublicId", userPublicId)
                   .param("comment", comment)
                   .update();
    }

    public int deleteComment(UUID userPublicId, UUID commentPublicId) {
        return jdbc.sql(DELETE_COMMENT_QUERY)
                   .param("userPublicId", userPublicId)
                   .param("commentPublicId", commentPublicId)
                   .update();
    }

    public int updateTicket(UUID userPublicId, UpdateTicketDTO request, OffsetDateTime dueDate) {
        return jdbc.sql(UPDATE_TICKET_QUERY)
                   .param("ticketPublicId", request.ticketPublicId())
                   .param("title", request.title())
                   .param("description", request.description())
                   .param("type", request.type().getValue(), Types.VARCHAR)
                   .param("status", request.status().getValue(), Types.VARCHAR)
                   .param("priority", request.priority().getValue(), Types.VARCHAR)
                   .param("dueDate", dueDate)
                   .param("progress", request.progress())
                   .param("userPublicId", userPublicId)
                   .update();
    }

    public int updateAssigneeForTicket(UUID assigneePublicId, UUID ticketPublicId) {
        return jdbc.sql(UPDATE_ASSIGNEE_TICKET_QUERY)
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
                           TicketStatus.valueOf(rs.getString("status")),
                           rs.getString("first_name"),
                           rs.getString("last_name"),
                           rs.getString("image_url"),
                           rs.getObject("created_at", OffsetDateTime.class),
                           rs.getObject("updated_at", OffsetDateTime.class)
                   ))
                   .single();
    }

    public int updateTask(UUID userPublicId, UpdateTaskDTO request) {
        return jdbc.sql(UPDATE_TASK_QUERY)
                   .param("taskPublicId", request.taskPublicId())
                   .param("userPublicId", userPublicId)
                   .param("name", request.name())
                   .param("description", request.description())
                   .param("status", request.status())
                   .update();
    }

    public int deleteTask(UUID userPublicId, UUID taskPublicId) {
        return jdbc.sql(DELETE_TASK_QUERY)
                   .param("userPublicId", userPublicId)
                   .param("taskPublicId", taskPublicId)
                   .update();
    }

    public List<TicketReportDTO> generateReportForUser(UUID userPublicId, CreateReportDTO request) {
        var query = createUserTicketReportQuery(request);
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
                           TicketStatus.valueOf(rs.getString("status")),
                           TicketPriority.valueOf(rs.getString("priority")),
                           TicketType.valueOf(rs.getString("type")),
                           rs.getObject("due_date", OffsetDateTime.class),
                           rs.getObject("created_at", OffsetDateTime.class),
                           rs.getObject("updated_at", OffsetDateTime.class)
                   ))
                   .list();

    }

    public List<TicketReportDTO> generateReport(CreateReportDTO request) {
        var query = createTicketReportQuery(request);
        return jdbc.sql(query)
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
                           TicketStatus.valueOf(rs.getString("status")),
                           TicketPriority.valueOf(rs.getString("priority")),
                           TicketType.valueOf(rs.getString("type")),
                           rs.getObject("due_date", OffsetDateTime.class),
                           rs.getObject("created_at", OffsetDateTime.class),
                           rs.getObject("updated_at", OffsetDateTime.class)
                   ))
                   .list();

    }

    public PageTicketDTO getUserTicketByTicketPublicId(UUID userPublicId, UUID ticketPublicId) {
        return jdbc.sql(SELECT_TICKET_BY_ISSUER_PUBLIC_ID_AND_TICKET_PUBLIC_ID_QUERY)
                   .param("userPublicId", userPublicId)
                   .param("ticketPublicId", ticketPublicId)
                   .query(PageTicketDTO.class)
                   .single();
    }
}
