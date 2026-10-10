package com.kamsan.ticketservice.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "user")
public record UserProperties(String imagesFolder) {

}
