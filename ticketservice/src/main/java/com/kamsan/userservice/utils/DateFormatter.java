package com.kamsan.userservice.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {

    public static String shortDate(OffsetDateTime date) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return date.format(formatter);
    }
}
