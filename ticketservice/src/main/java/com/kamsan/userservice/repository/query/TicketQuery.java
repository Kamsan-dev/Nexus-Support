package com.kamsan.userservice.repository.query;

public class TicketQuery {

    private TicketQuery() {
    }

    public static final String SELECT_TICKETS_BY_ISSUER_PUBLIC_ID_QUERY = """
                    SELECT
                        COUNT(DISTINCT(c.comment_id)) AS comment_count,
                        COUNT(DISTINCT(f.file_id)) AS file_count,
                        t.ticket_public_id,
                        t.title,
                        t.description,
                        t.progress,
                        t.due_date,
                        t.created_at,
                        t.updated_at,
                        s.status,
                        typ.type,
                        pr.priority
                    FROM tickets t
                    JOIN users u ON u.user_id = t.issuer_id
                    JOIN statuses s ON s.status_id = t.ticket_id
                    JOIN types typ ON typ.type_id = t.type_id
                    JOIN priorities pr ON pr.priority_id = t.priority_id
                    LEFT JOIN files f ON t.ticket_id = f.ticket_id
                    LEFT JOIN comments c ON t.ticket_id = c.ticket_id
                    WHERE u.user_public_id = :userPublicId
            """;

    public static final String SELECT_TICKETS_PAGE_NUMBER_QUERY = """
                    SELECT
                        (CEILING(COUNT(*) / :size ::NUMERIC(10, 5))) AS pages 
                        FROM tickets t
                    JOIN users u ON u.user_id = t.issuer_id
                    JOIN statuses s ON s.status_id = t.ticket_id
                    JOIN types typ ON typ.type_id = t.type_id
                    JOIN priorities pr ON pr.priority_id = t.priority_id
                    WHERE u.user_public_id = :userPublicId
            """;
}
