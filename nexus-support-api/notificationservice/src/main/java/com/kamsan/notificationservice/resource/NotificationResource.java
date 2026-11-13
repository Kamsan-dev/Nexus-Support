package com.kamsan.notificationservice.resource;

import com.kamsan.notificationservice.domain.ApiResponse;
import com.kamsan.notificationservice.dto.*;
import com.kamsan.notificationservice.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.kamsan.notificationservice.utils.RequestUtils.getResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationResource {

    private final MessageService messageService;

    @GetMapping("/message/get-all")
    public ResponseEntity<ApiResponse<ConversationOverviewsDTO>> getAllMessages(@NotNull Authentication authentication) {
        return ResponseEntity.ok().body(getResponse(
                messageService.getMessages(UUID.fromString(authentication.getName())),
                "Messages retrieved.",
                HttpStatus.OK
        ));
    }

    @GetMapping("/message/recipients")
    public ResponseEntity<ApiResponse<List<RecipientDTO>>> getAllRecipients(@NotNull Authentication authentication) {
        return ResponseEntity.ok().body(getResponse(
                messageService.getAllRecipients(UUID.fromString(authentication.getName())),
                "Recipients retrieved.",
                HttpStatus.OK
        ));
    }

    @GetMapping("/message/{conversationId}")
    public ResponseEntity<ApiResponse<ConversationMessagesDTO>> getConversationMessages(@NotNull Authentication authentication, @PathVariable("conversationId") UUID conversationId) {
        return ResponseEntity.ok().body(getResponse(
                messageService.getConversation(UUID.fromString(authentication.getName()), conversationId),
                "Messages retrieved.",
                HttpStatus.OK
        ));
    }

    @PostMapping("/message")
    public ResponseEntity<ApiResponse<MessageDTO>> sendMessage(@NotNull Authentication authentication, @RequestBody @Valid SendMessageDTO sendMessageDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(getResponse(
                messageService.sendMessage(UUID.fromString(authentication.getName()),
                        sendMessageDTO),
                "Message delivered.",
                HttpStatus.CREATED
        ));
    }

    @PostMapping("/message/{conversationId}")
    public ResponseEntity<ApiResponse<MessageDTO>> sendMessageInConversation(@NotNull Authentication authentication,
                                                                                          @PathVariable("conversationId") UUID conversationId,
                                                                                          @RequestBody @Valid SendMessageDTO sendMessageDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(getResponse(
                messageService.sendMessageInConversation(UUID.fromString(authentication.getName()),
                        sendMessageDTO, conversationId),
                "Message delivered.",
                HttpStatus.CREATED
        ));
    }
}
