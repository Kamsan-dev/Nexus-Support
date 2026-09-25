package com.kamsan.notificationservice.repository.query;

public class MessageQuery {

    private MessageQuery() {
    }

    public static final String INSERT_MESSAGE_QUERY = """
                            INSERT INTO messages m (
                                m.message_public_id,
                                m.conversation_id,
                                m.subject,
                                m.content,
                                m.sender_id,
                                m.receiver_id
                            )
                            SELECT
                            :messagePublicId,
                            :conversationId,
                            :subject,
                            :content,
                            s.sender_id,
                            r.receiver_id
                            FROM users s
                            WHERE s.user_public_id = :userPublicId
                            JOIN users r ON r.email = :toEmail
                            RETURNING *
            """;

    public static final String INSERT_MESSAGE_STATUS_QUERY = """
                                    INSERT INTO message_statuses m (
                                        m.message_id,
                                        m.user_id,
                                        m.message_status,
                                    VALUES
                                    (:messageId, :senderId, 'READ'),
                                    (:messageId, :receiverId, 'UNREAD');
            """;

    public static final String SELECT_MESSAGE_QUERY = """
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
            m.subject,
            m.content,
            m.created_at,
            m.updated_at,
            ms.message_status AS status
            FROM messages m
            JOIN users s ON s.user_id = m.sender_id
            JOIN users r ON r.user_id = m.receiver_id
            JOIN message_statuses ms
            ON (ms.message_id = m.message_id AND ms.user_id = s.user_id)
            WHERE m.message_id = :messageId
            AND s.user_public_id = :userPublicId
            """;

    public static final String SELECT_MESSAGES_QUERY = """
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
                m.subject,
                m.content,
                m.created_at,
                m.updated_at,
                ms.message_status AS status
            FROM messages m
            JOIN users s ON s.user_id = m.sender_id
            JOIN users r ON r.user_id = m.receiver_id
            JOIN message_statuses ms
            ON (ms.message_id = m.message_id AND ms.user_id = (SELECT user_id FROM users WHERE user_public_id = :userPublicId))
            WHERE s.user_public_id = :userPublicId
            OR r.user_public_id = :userPublicId
            ORDER BY m.created_at DESC
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
                m.subject,
                m.content,
                m.created_at,
                m.updated_at,
                ms.message_status AS status
            FROM messages m
            JOIN users s ON s.user_id = m.sender_id
            JOIN users r ON r.user_id = m.receiver_id
            JOIN message_statuses ms
            ON (ms.message_id = m.message_id AND ms.user_id = (SELECT user_id FROM users WHERE user_public_id = :userPublicId))
            WHERE (s.user_public_id = :userPublicId OR r.user_public_id = :userPublicId)
            AND m.conversation_id = :conversationId
            ORDER BY m.created_at DESC
            """;
}
