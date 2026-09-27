package com.kamsan.notificationservice.utils;

import java.util.UUID;
import java.util.function.Supplier;

public class NotificationUtils {

    private NotificationUtils() {
    }

    public static Supplier<UUID> randomUUID = UUID::randomUUID;

    public static String getVerificationUrl(String host, String token) {
        return host + "/verification/account?token=" + token;
    }

    public static String getResetPasswordUrl(String host, String token) {
        return host + "/verification/password?token=" + token;
    }

    public static String getTicketUrl(String host, String ticketNumber) {
        return host + "/ticket/" + ticketNumber;
    }
}
