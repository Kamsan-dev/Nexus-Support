package com.kamsan.ticketservice.repository.query;

public class TicketDashboardQuery {

    private TicketDashboardQuery(){}

    public static final String TICKET_BY_STATUS_QUERY = """
            SELECT
                COUNT(t.ticket_id) AS ticket_count,
                s.status
            FROM statuses s
            LEFT JOIN tickets t ON s.status_id = t.status_id
            GROUP BY s.status
            ORDER BY s.status;
            """;

    public static final String TICKET_BY_TYPE_QUERY = """
            SELECT
                COUNT(t.ticket_id) AS ticket_count,
                typ.type
            FROM types typ
            LEFT JOIN  tickets t ON typ.type_id = t.type_id
            GROUP BY typ.type
            ORDER BY typ.type;
            """;
    public static final String TICKET_COUNT_QUERY = """
            SELECT
                COUNT(t.ticket_id) AS ticket_count
            FROM tickets t;
            """;

    public static final String TICKET_TREND_WEEKLY_QUERY = """
            SELECT
                DATE_TRUNC('week', t.created_at)::date AS week_start,
                COUNT(*) FILTER (WHERE s.status = 'NEW') AS new_count,
                COUNT(*) FILTER (WHERE s.status = 'COMPLETED') AS completed_count
            FROM tickets t
            JOIN statuses s ON s.status_id = t.status_id
            WHERE t.created_at >= CURRENT_DATE - INTERVAL '3 months'
              AND s.status IN ('NEW', 'COMPLETED')
            GROUP BY DATE_TRUNC('week', t.created_at)
            ORDER BY week_start;
            """;
}
