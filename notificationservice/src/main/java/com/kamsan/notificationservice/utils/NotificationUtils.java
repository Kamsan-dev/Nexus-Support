package com.kamsan.notificationservice.utils;

import java.util.UUID;
import java.util.function.Supplier;

public class NotificationUtils {

    private NotificationUtils() {
    }

    public static Supplier<UUID> randomUUID = UUID::randomUUID;
}
