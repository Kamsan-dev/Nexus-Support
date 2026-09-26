package com.kamsan.notificationservice.resource;

import com.kamsan.notificationservice.service.implementation.MessageServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("/notification")
public class NotificationResource {

    private final MessageServiceImpl userService;

    private URI getUri() {
        return URI.create("/profile/<userId>");
    }

}
