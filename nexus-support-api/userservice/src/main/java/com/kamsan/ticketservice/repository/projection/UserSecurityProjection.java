package com.kamsan.ticketservice.repository.projection;

public interface UserSecurityProjection {

    String getPassword();

    String getRole();

    String getAuthorities();

    Boolean getCredentialsExpired();
}
