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
            RETURNING ticket_public_id;
            """;

    public static final String SELECT_TICKET_BY_USER_AND_TICKET_PUBLIC_ID_QUERY = """
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

    public static final String SELECT_COMMENTS_BY_TICKET_PUBLIC_ID_QUERY = """
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

    public static final String SELECT_TASKS_BY_TICKET_PUBLIC_ID_QUERY = """
            SELECT
                ta.task_public_id,
                ta.name,
                ta.description,
                ta.due_date,
                ta.updated_at,
                ta.created_at,
                s.status,
                a.user_public_id AS assignee_public_id,
                a.first_name,
                a.last_name,
                a.image_url,
            FROM tasks ta
            JOIN tickets t ON t.ticket_id = ta.ticket_id
            LEFT JOIN users a ON a.user_id = ta.assignee_id
            JOIN statuses s ON s.status_id = ta.status_id
            WHERE t.ticket_public_id = :ticketPublic
            ORDER BY ta.created_at DESC
            """;

    public static final String INSERT_COMMENT_TICKET_QUERY = """
            INSERT INTO comments (
                 comment_public_id,
                 user_id,
                 ticket_id,
                 comment
                 )
             SELECT
                 :commentPublicId,
                 u.user_id,
                 t.ticket_id,
                 :comment
             FROM users u
             JOIN tickets t ON t.ticket_public_id = :ticketPublicId
             WHERE u.user_public_id = :userPublicId
             RETURNING comment_public_id
            """;

    public static final String SELECT_FILES_TICKET_QUERY = """
            SELECT f.*
            FROM files f
            JOIN tickets t ON t.ticket_id = f.ticket_id
            WHERE t.ticket_public_id = :ticketPublicId
            """;

    public static final String DELETE_FILE_QUERY = """
            DELETE FROM files
            WHERE file_public_id = :filePublicId
            """;
    public static final String UPDATE_COMMENT_QUERY = """
            UPDATE comments
            SET comment = :comment,
            updated_at = NOW()
            WHERE comment_public_id = :commentPublicId
            """;
    public static final String DELETE_COMMENT_QUERY = """
            DELETE FROM comments
            WHERE comment_public_id = :commentPublicId
            """;

    public static final String UPDATE_TICKET_QUERY = """
            UPDATE tickets t
            SET t.title = :title,
            t.description = :description,
            t.progress = :progress,
            t.type_id = typ.type_id,
            t.priority_id = p.priority_id,
            t.status_id = s.status_id,
            t.due_date = :dueDate,
            t.updated_at = NOW()
            JOIN statuses s ON s.status = :status
            JOIN types typ ON typ.type = :type
            JOIN priorities p ON p.priority = :priority
            WHERE t.ticket_public_id = :ticketPublicId
            """;

    public static final String UPDATE_ASSIGNEE_TICKET_QUERY = """
            UPDATE tickets t
            t.assignee_id = a.user_id
            t.updated_at = NOW()
            LEFT JOIN users a ON a.user_public_id = :assigneePublicId
            WHERE t.ticket_public_id = :ticketPublicId
            """;

    public static final String INSERT_TICKET_TASK_QUERY = """
            WITH task AS (
                INSERT INTO tasks (
                    task_public_id,
                    ticket_id,
                    assignee_id,
                    name,
                    description,
                    status_id
                )
                SELECT
                    :taskPublicId,
                    t.ticket_id,
                    a.user_id,
                    :name,
                    :description,
                    s.status_id
                FROM users a
                JOIN tickets t ON t.ticket_public_id = :ticketPublicId
                JOIN statuses s ON s.status = :status
                WHERE a.user_public_id = :assigneePublicId
                RETURNING *
            )
            SELECT
                i.name,
                i.description,
                s.status,
                i.due_date,
                a.first_name,
                a.last_name,
                a.image_url,
                i.created_at,
                i.updated_at
            FROM task i
            JOIN statuses s ON s.status_id = i.status_id
            JOIN users a ON a.user_id = i.assignee_id;
            """;
    
    public static final String SELECT_TICKET_FOR_REPORT_QUERY = """
            SELECT
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
            WHERE u.user_public_id = :userPublicId
            """;
}
