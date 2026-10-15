package com.kamsan.ticketservice.utils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateFormatter {

    private static final ZoneId PARIS_ZONE = ZoneId.of("Europe/Paris");

    public static String shortDate(OffsetDateTime date) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return date.format(formatter);
    }

    public static OffsetDateTime toOffsetDateTime(LocalDate date) {
        return date == null
                ? null
                : date.atStartOfDay(PARIS_ZONE).toOffsetDateTime();
    }
}
