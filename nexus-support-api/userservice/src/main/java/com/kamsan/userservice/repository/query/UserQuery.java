package com.kamsan.userservice.repository.query;

public class UserQuery {

    private UserQuery() {
    }

    public static final String FIND_USERS_PAGE_QUERY = """
            SELECT
                u.user_public_id,
                u.email,
                u.first_name,
                u.last_name,
                u.member_id,
                u.bio,
                u.image_url,
                u.phone,
                u.address,
                u.created_at,
                r.name AS role,
                r.authority AS authorities
            FROM users u
            JOIN user_roles ur ON ur.user_id = u.user_id
            JOIN roles r ON r.role_id = ur.role_id
            ORDER BY u.user_id
            LIMIT :limit OFFSET :offset
            """;

    public static final String COUNT_TOTAL_USERS_QUERY = """
            SELECT COUNT(*)
            FROM users
            """;

    public static final String SELECT_TICKET_ASSIGNEE_QUERY = """
            SELECT
                u.user_public_id,
                u.email,
                u.first_name,
                u.last_name,
                u.imageUrl,
                r.name
            FROM users u
            JOIN user_roles ur ON ur.user_id = u.user_id
            JOIN roles r ON ur.role_id = r.role_id
            JOIN tickets t ON u_user_id = t.assignee_id
            WHERE t.ticket_public_id = :ticketPublicId
            """;

    public static final String SELECT_TECH_SUPPORTS_QUERY = """
            SELECT
                u.user_public_id,
                u.email,
                u.first_name,
                u.last_name,
                u.image_url,
                r.name
            FROM users u
            JOIN user_roles ur ON ur.user_id = u.user_id
            JOIN roles r ON ur.role_id = r.role_id
            WHERE r.name = 'TECH_SUPPORTS'
            """;

    public static final String SELECT_DEVICES_QUERY = """
            SELECT
                d.machine,
                d.client,
                d.ip_address,
                d.created_at
            FROM devices d
            JOIN users u
                ON u.user_id = d.user_id
            WHERE u.user_public_id = :userPublicId
            ORDER BY d.created_at DESC
            LIMIT 10
            """;
}
