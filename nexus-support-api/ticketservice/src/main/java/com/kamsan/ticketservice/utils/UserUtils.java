package com.kamsan.ticketservice.utils;

import com.kamsan.ticketservice.enumeration.Role;

public class UserUtils {

    private UserUtils() {
    }

    public static boolean hasElevatedPermissions(Role role) {
        return role == Role.ADMIN ||
                role == Role.SUPER_ADMIN ||
                role == Role.TECH_SUPPORT ||
                role == Role.MANAGER;
    }
}
