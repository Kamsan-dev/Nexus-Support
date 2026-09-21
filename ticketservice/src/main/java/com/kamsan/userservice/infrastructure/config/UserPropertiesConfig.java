package com.kamsan.userservice.infrastructure.config;

import com.kamsan.userservice.domain.TicketProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TicketProperties.class)
public class UserPropertiesConfig {
}
