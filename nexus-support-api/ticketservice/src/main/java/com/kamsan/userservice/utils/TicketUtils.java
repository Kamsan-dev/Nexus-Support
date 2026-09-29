package com.kamsan.userservice.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.function.Supplier;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Slf4j
public class TicketUtils {

    private TicketUtils() {
    }

    public static Supplier<UUID> randomUUID = UUID::randomUUID;

    public static String getFileUri(String filename) {
        String uriString = fromCurrentContextPath().path("/ticket/files/" + filename).toUriString();
        log.info("File uri : {}", uriString);
        return uriString;
    }
}
