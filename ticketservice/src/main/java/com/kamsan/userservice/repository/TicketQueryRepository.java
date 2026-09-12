package com.kamsan.userservice.repository;

import com.kamsan.userservice.dto.PageTicketDTO;
import com.kamsan.userservice.dto.PageTicketRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

import static com.kamsan.userservice.repository.query.TicketQuery.SELECT_TICKETS_BY_ISSUER_PUBLIC_ID_QUERY;

@Service
@RequiredArgsConstructor
public class TicketQueryRepository {

    private final JdbcClient jdbc;

    public List<PageTicketDTO> getTicketsPage(UUID userPublicId, PageTicketRequestDTO request) {
        return jdbc.sql(SELECT_TICKETS_BY_ISSUER_PUBLIC_ID_QUERY)
                   .param("size", request.size())
                   .param("offset", getOffset.apply(request.page(), request.size()))
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

    public int getPages(UUID userPublicId, PageTicketRequestDTO request) {
        return jdbc.sql(SELECT_TICKETS_BY_ISSUER_PUBLIC_ID_QUERY)
                   .param("size", request.size())
                   .param("status", request.status())
                   .param("type", request.type())
                   .param("filter", request.filter())
                   .param("userPublicId", userPublicId)
                   .query(Integer.class)
                   .single();
    }

    private final BiFunction<Integer, Integer, Integer> getOffset = (page, size) -> page * size;

}
