package com.kamsan.userservice.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ticket")
public record TicketProperties(String filesDirectory) {

}
