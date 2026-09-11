package com.kamsan.userservice.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record CreateReportDTO(
        String filter,
        OffsetDateTime fromDate,
        OffsetDateTime toDate,
        List<String> statuses,
        List<String> priorities,
        List<String> types
) {
}
