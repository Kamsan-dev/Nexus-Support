package com.kamsan.ticketservice.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ticket")
public record TicketProperties(String filesDirectory) {

}
