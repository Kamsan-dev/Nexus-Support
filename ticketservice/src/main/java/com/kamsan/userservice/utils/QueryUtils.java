package com.kamsan.userservice.utils;

import com.kamsan.userservice.dto.CreateReportDTO;

import static com.kamsan.userservice.repository.query.TicketQuery.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.replace;

public class QueryUtils {

    public static final String FILTER_TITLE_BY_CRITERIA = " AND t.title ~* :filter";

    public static String createSelectTicketsQuery(String status, String type, String filter) {
        var query = getStringBuilder(SELECT_TICKETS_BY_ISSUER_PUBLIC_ID_QUERY);
        if (isNotBlank(status)) {
            query.append(" AND s.status = :status");
        }
        if (isNotBlank(type)) {
            query.append(" AND typ.type = :type");
        }
        if (isNotBlank(filter)) {
            query.append(FILTER_TITLE_BY_CRITERIA);
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
            query.append(FILTER_TITLE_BY_CRITERIA);
        }

        return replace(query.toString(), "\\n", "");
    }

    public static final String createTicketReportQuery(CreateReportDTO request) {
        var query = getStringBuilder(SELECT_TICKET_FOR_REPORT_QUERY);

        if (isNotBlank(request.fromDate().toString())) {
            query.append(" AND t.created_at >= :fromDate");
        }

        if (isNotBlank(request.toDate().toString())) {
            query.append(" AND t.created_at <= :toDate");
        }
        if (!request.statuses().isEmpty()) {
            query.append(" AND s.status IN (:statuses)");
        }
        if (!request.types().isEmpty()) {
            query.append(" AND typ.type IN (:types)");
        }
        if (!request.priorities().isEmpty()) {
            query.append(" AND p.priority IN (:priorities)");
        }
        if (isNotBlank(request.filter())) {
            query.append(FILTER_TITLE_BY_CRITERIA);
        }

        query.append(" ORDER BY t.created_at DESC");

        return replace(query.toString(), "\\n", "");
    }

    private static StringBuilder getStringBuilder(String query) {
        return new StringBuilder(query);
    }

}
