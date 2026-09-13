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
            JOIN statuses s ON s.status_id = t.status_id
            JOIN types typ ON typ.type_id = t.type_id
            JOIN priorities pr ON pr.priority_id = t.priority_id
            LEFT JOIN files f ON t.ticket_id = f.ticket_id
            LEFT JOIN comments c ON t.ticket_id = c.ticket_id
            WHERE u.user_public_id = :userPublicId
            """;

    public static final String SELECT_COUNT_TICKET_NUMBER_QUERY = """
            SELECT
                COUNT(*)
                FROM tickets t
                JOIN users u ON u.user_id = t.issuer_id
                JOIN statuses s ON s.status_id = t.status_id
                JOIN types typ ON typ.type_id = t.type_id
                JOIN priorities pr ON pr.priority_id = t.priority_id
            WHERE u.user_public_id = :userPublicId
            """;

    public static final String INSERT_TICKET_QUERY = """
            INSERT INTO tickets (
                ticket_public_id,
                issuer_id,
                title,
                description,
                status_id,
                priority_id,
                type_id )
            SELECT
                :ticketPublicId,
                u.user_id,
                :title,
                :description,
                s.status_id,
                p.priority_id,
                typ.type_id
            FROM users u
            JOIN statuses s ON s.status = :status
            JOIN priorities p ON p.priority = :priority
            JOIN types typ ON typ.type = :type
            WHERE u.user_public_id = :userPublicId
            RETURNING *
            """;

    public static final String SELECT_TICKET_BY_USER_AND_TICKET_PUBLIC_ID = """
            SELECT
                 t.ticket_public_id,
                 a.user_public_id AS assignee_public_id,
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
             LEFT JOIN users a ON a.user_id = t.assignee_id
             JOIN statuses s ON s.status_id = t.status_id
             JOIN types typ ON typ.type_id = t.type_id
             JOIN priorities pr ON pr.priority_id = t.priority_id
             WHERE u.user_public_id = :userPublicId
             AND t.ticket_public_id = :ticketPublicId
            """;

    public static final String SELECT_COMMENTS_BY_TICKET_PUBLIC_ID = """
            SELECT
                c.comment_public_id,
                u.user_public_id,
                u.first_name,
                u.last_name,
                u.image_url,
                c.comment,
                c.updated_at,
                c.created_at,
                c.created_at <> c.updated_at AS is_edited
            FROM comments c
            JOIN tickets t ON t.ticket_id = c.ticket_id
            JOIN users u ON c.user_id = u.user_id
            WHERE t.ticket_public_id = :ticketPublic
            ORDER BY c.created_at DESC
            """;
}
