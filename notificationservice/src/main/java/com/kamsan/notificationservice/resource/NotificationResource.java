package com.kamsan.notificationservice.resource;

import com.kamsan.notificationservice.domain.ApiResponse;
import com.kamsan.notificationservice.dto.MessageDTO;
import com.kamsan.notificationservice.dto.SendMessageDTO;
import com.kamsan.notificationservice.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.kamsan.notificationservice.utils.RequestUtils.getResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/notification")
public class NotificationResource {

    private final MessageService messageService;

    @PostMapping("/message")
    public ResponseEntity<ApiResponse<UUID>> sendMessage(@NotNull Authentication authentication, @RequestBody @Valid SendMessageDTO sendMessageDTO) {
        UUID messageUUID = messageService.sendMessage(UUID.fromString(authentication.getName()), sendMessageDTO);
        return ResponseEntity.created(getUri()).body(getResponse(
                messageUUID,
                "Message delivered.",
                HttpStatus.CREATED
        ));
    }

    @GetMapping("/message/get-all")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getAllMessages(@NotNull Authentication authentication) {
        return ResponseEntity.ok().body(getResponse(
                messageService.getMessages(UUID.fromString(authentication.getName())),
                "Messages retrieved.",
                HttpStatus.OK
        ));
    }

    @GetMapping("/message/{conversationId}")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getAllMessages(@NotNull Authentication authentication, @PathVariable("conversationId") UUID conversationId) {
        return ResponseEntity.ok().body(getResponse(
                messageService.getConversation(UUID.fromString(authentication.getName()), conversationId),
                "Messages retrieved.",
                HttpStatus.OK
        ));
    }

    private URI getUri() {
        return URI.create("/profile/<userId>");
    }

}
