package com.kamsan.notificationservice.repository.query;

public class MessageQuery {

    private MessageQuery() {
    }

    public static final String INSERT_MESSAGE_QUERY = """
            INSERT INTO messages (
                message_public_id,
                conversation_id,
                content,
                sender_id,
                receiver_id
            )
            SELECT
            :messagePublicId,
            :conversationId,
            :content,
            s.user_id,
            r.user_id
            FROM users s
            JOIN users r ON r.user_public_id = :receiverPublicId
            WHERE s.user_public_id = :userPublicId
            RETURNING *
            """;

    public static final String INSERT_MESSAGE_STATUS_QUERY = """
            INSERT INTO message_statuses (
                message_id,
                user_id,
                message_status
            )
            VALUES
            (:messageId, :senderId, 'READ'),
            (:messageId, :receiverId, 'UNREAD');
            """;

    public static final String SELECT_MESSAGES_BY_CONVERSATION_ID_QUERY = """
            SELECT
                s.user_public_id AS senderPublicId,
                s.first_name AS senderFirstname,
                s.last_name AS senderLastname,
                s.email AS senderEmail,
                s.image_url AS senderImageUrl,
                r.user_public_id AS receiverPublicId,
                r.first_name AS receiverFirstname,
                r.last_name AS receiverLastname,
                r.email AS receiverEmail,
                r.image_url AS receiverImageUrl,
                ms.message_status AS status,
                m.message_public_id,
                m.message_id,
                m.subject,
                m.content,
                m.created_at,
                m.updated_at
            FROM messages m
            JOIN users s ON s.user_id = m.sender_id
            JOIN users r ON r.user_id = m.receiver_id
            JOIN message_statuses ms
            ON (ms.message_id = m.message_id AND ms.user_id = (SELECT user_id FROM users WHERE user_public_id = :userPublicId))
            WHERE (s.user_public_id = :userPublicId OR r.user_public_id = :userPublicId)
            AND m.conversation_id = :conversationId
            ORDER BY m.created_at ASC;
            """;

    public static final String SELECT_MESSAGE_STATUS_QUERY = """
            SELECT ms.message_status
            FROM message_statuses ms
            WHERE ms.user_id = (SELECT user_id FROM users WHERE user_public_id = :userPublicId)
            AND ms.message_id = :messageId
            """;

    public static final String SELECT_CONVERSATION_ID_QUERY = """
            SELECT m.conversation_id
                FROM messages m
                JOIN users sender
                    ON sender.user_id = m.sender_id
                JOIN users receiver
                    ON receiver.user_id = m.receiver_id
                WHERE (
                    sender.user_public_id = :authenticatedUserPublicId
                    AND receiver.user_public_id = :receiverPublicId
                )
                OR (
                    receiver.user_public_id = :authenticatedUserPublicId
                    AND sender.user_public_id = :receiverPublicId
                )
                LIMIT 1
            """;


    public static final String SELECT_UNREAD_MESSAGE_COUNT_QUERY = """
            SELECT COUNT(*) AS unreadMessagesCount
            FROM messages m
            JOIN message_statuses ms ON ms.message_id = m.message_id
            JOIN users r ON r.user_public_id = :userPublicId
            WHERE ms.message_status = 'UNREAD'
            AND m.receiver_id = r.user_id;
            """;

    public static final String SELECT_CONVERSATION_OVERVIEW_QUERY = """
            WITH authenticated_user AS (
                SELECT user_id
                FROM users
                WHERE user_public_id = :userPublicId
            ),
            ranked_messages AS (
                SELECT
                    m.message_id,
                    ROW_NUMBER() OVER (
                        PARTITION BY m.conversation_id
                        ORDER BY m.created_at DESC, m.message_id DESC
                    ) AS rn
                FROM messages m
                CROSS JOIN authenticated_user au
                WHERE m.sender_id = au.user_id
                OR m.receiver_id = au.user_id
            )
            
            SELECT
                m.conversation_id,
                m.message_public_id,
                m.content AS last_message_content,
                ms.message_status AS last_message_status,
                m.created_at,
                m.updated_at,
                s.user_public_id AS senderPublicId,
                s.first_name AS senderFirstname,
                s.last_name AS senderLastname,
                s.email AS senderEmail,
                s.image_url AS senderImageUrl,
                r.user_public_id AS receiverPublicId,
                r.first_name AS receiverFirstname,
                r.last_name AS receiverLastname,
                r.email AS receiverEmail,
                r.image_url AS receiverImageUrl,
            
                CASE
                    WHEN m.sender_id = au.user_id
                        THEN CONCAT('You & ', r.first_name, ' ', r.last_name)
                    ELSE CONCAT(s.first_name, ' ', s.last_name, ' & You')
                END AS title
            
            FROM ranked_messages rm
            
            JOIN messages m ON m.message_id = rm.message_id
            CROSS JOIN authenticated_user au
            JOIN message_statuses ms ON (ms.message_id = m.message_id AND ms.user_id = au.user_id)
            JOIN users s ON s.user_id = m.sender_id
            JOIN users r ON r.user_id = m.receiver_id
            
            WHERE rm.rn = 1
            ORDER BY m.created_at DESC;
            """;

    public static final String SELECT_CONVERSATION_OVERVIEW_BY_CONVERSATION_ID_QUERY = """
            SELECT
                m.conversation_id,
                m.message_public_id,
                m.content AS last_message_content,
                ms.message_status AS last_message_status,
                m.created_at,
                m.updated_at,
                s.user_public_id AS senderPublicId,
                s.first_name AS senderFirstname,
                s.last_name AS senderLastname,
                s.email AS senderEmail,
                s.image_url AS senderImageUrl,
                r.user_public_id AS receiverPublicId,
                r.first_name AS receiverFirstname,
                r.last_name AS receiverLastname,
                r.email AS receiverEmail,
                r.image_url AS receiverImageUrl,
                CONCAT('You & ', r.first_name, ' ', r.last_name) AS title
            FROM messages m
            JOIN message_statuses ms
            ON ms.message_id = m.message_id
            AND ms.user_id = (SELECT user_id FROM users WHERE user_public_id = :authenticatedUserPublicId)
            JOIN users s ON s.user_id = m.sender_id
            JOIN users r ON r.user_id = m.receiver_id
            WHERE m.conversation_id = :conversationId
            AND s.user_public_id = :authenticatedUserPublicId
            AND r.user_public_id = :receiverPublicId
            ORDER BY m.created_at DESC
            LIMIT 1
            """;


    public static final String UPDATE_MESSAGE_STATUS_QUERY = """
            UPDATE message_statuses
            SET message_status = 'READ'
            WHERE user_id = (
                SELECT user_id
                FROM users
                WHERE user_public_id = :userPublicId)
              AND message_id IN (
                  SELECT message_id
                  FROM messages
                  WHERE conversation_id = :conversationId
              )
              AND message_status = 'UNREAD'
            RETURNING message_id
            """;

    public static final String SELECT_RECIPIENTS_QUERY = """
            SELECT
                u.user_public_id,
                u.first_name,
                u.last_name,
                u.image_url,
                r.name AS role
            FROM users u
            JOIN user_roles ur ON ur.user_id = u.user_id
            JOIN roles r ON r.role_id = ur.role_id
            ORDER by r.name
            """;
}
