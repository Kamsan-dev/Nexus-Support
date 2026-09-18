package com.kamsan.userservice.resource;

import com.kamsan.userservice.domain.ApiResponse;
import com.kamsan.userservice.dto.*;
import com.kamsan.userservice.service.TicketService;
import com.kamsan.userservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.kamsan.userservice.utils.RequestUtils.getResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/ticket")
public class TicketResource {

    private final UserService userService;
    private final TicketService ticketService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Page<PageTicketDTO>>> getTickets(@NotNull Authentication authentication, @RequestBody PageTicketRequestDTO pageTicketRequestDTO) {
        Page<PageTicketDTO> tickets = ticketService.getTickets(UUID.fromString(authentication.getName()),
                pageTicketRequestDTO);
        return ResponseEntity.ok().body(getResponse(
                tickets,
                "Tickets retrieved.",
                HttpStatus.OK));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UUID>> createTicket(@NotNull Authentication authentication,
                                                          @RequestPart @Valid CreateTicketDTO createTicketDTO,
                                                          @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        UUID ticketPublicId = this.ticketService.createTicket(UUID.fromString(authentication.getName()),
                createTicketDTO,
                files);
        return ResponseEntity.created(getUri()).body(getResponse(
                ticketPublicId,
                "Ticket created successfully.",
                HttpStatus.CREATED));
    }

    @GetMapping("/{ticketPublicId}")
    public ResponseEntity<ApiResponse<TicketDTO>> getTicket(@NotNull Authentication authentication,
                                                            @PathVariable("ticketPublicId") UUID ticketPublicId) {
        TicketDetailsDTO userTicket = ticketService.getUserTicket(UUID.fromString(authentication.getName()),
                ticketPublicId);
        List<CommentDTO> ticketComments = ticketService.getTicketComments(UUID.fromString(authentication.getName()));
        List<AttachmentDTO> ticketFiles = ticketService.getTicketFiles(ticketPublicId);
        List<TaskDTO> ticketTasks = ticketService.getTicketTasks(ticketPublicId);
        List<TicketUserDTO> techSupports = userService.getTechSupports();
        TicketUserDTO assignee = userService.getAssignee(ticketPublicId);
        ReadUserDTO connectedUser = userService.getUserByUUID(UUID.fromString(authentication.getName()));

        return ResponseEntity.ok().body(getResponse(
                new TicketDTO(userTicket,
                        ticketComments,
                        ticketFiles,
                        ticketTasks,
                        techSupports,
                        assignee,
                        connectedUser),
                "Ticket retrieved.",
                HttpStatus.OK));
    }

    private URI getUri() {
        return URI.create("/ticket/<ticketPublicId>");
    }

}
