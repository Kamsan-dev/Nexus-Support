package com.kamsan.userservice.utils;

import static com.kamsan.userservice.repository.query.TicketQuery.SELECT_COUNT_TICKET_NUMBER_QUERY;
import static com.kamsan.userservice.repository.query.TicketQuery.SELECT_TICKETS_BY_ISSUER_PUBLIC_ID_QUERY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.replace;

public class QueryUtils {

    public static String createSelectTicketsQuery(String status, String type, String filter) {
        var query = getStringBuilder(SELECT_TICKETS_BY_ISSUER_PUBLIC_ID_QUERY);
        if (isNotBlank(status)) {
            query.append(" AND s.status = :status");
        }
        if (isNotBlank(type)) {
            query.append(" AND typ.type = :type");
        }
        if (isNotBlank(filter)) {
            query.append(" AND t.title ~* :filter");
        }

        query.append("""
                        GROUP BY t.ticket_id, s.status, typ.type_id, pr.priority_id " +
                        "ORDER BY t.created_at DESC " +
                        "LIMIT :size " +
                        "OFFSET :offset
                """);

        return replace(query.toString(), "\\n", "");
    }

    public static String createSelectTotalElementsQuery(String status, String type, String filter) {
        var query = getStringBuilder(SELECT_COUNT_TICKET_NUMBER_QUERY);
        if (isNotBlank(status)) {
            query.append(" AND s.status = :status");
        }
        if (isNotBlank(type)) {
            query.append(" AND typ.type = :type");
        }
        if (isNotBlank(filter)) {
            query.append(" AND t.title ~* :filter");
        }

        return replace(query.toString(), "\\n", "");
    }

    private static StringBuilder getStringBuilder(String query) {
        return new StringBuilder(query);
    }

}
