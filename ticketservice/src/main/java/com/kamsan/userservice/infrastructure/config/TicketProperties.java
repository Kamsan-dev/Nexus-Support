package com.kamsan.userservice.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ticket")
public record TicketProperties(String filesDirectory) {

}
