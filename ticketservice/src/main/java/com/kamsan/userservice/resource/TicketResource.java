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
                HttpStatus.OK
        ));
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
                HttpStatus.CREATED
        ));
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
                HttpStatus.OK
        ));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Void>> updateTicket(@NotNull Authentication authentication,
                                                          @RequestBody @Valid UpdateTicketDTO updateTicketDTO) {
        this.ticketService.updateTicket(UUID.fromString(authentication.getName()), updateTicketDTO);
        return ResponseEntity.ok().body(getResponse(
                null,
                "Ticket updated successfully.",
                HttpStatus.OK
        ));
    }

    @PatchMapping("/update/assignee")
    public ResponseEntity<ApiResponse<Void>> updateTicket(@NotNull Authentication authentication,
                                                          @RequestParam("assigneePublicId") UUID assigneePublicId,
                                                          @RequestParam("ticketPublicId") UUID ticketPublicId) {
        this.ticketService.updateAssignee(UUID.fromString(authentication.getName()), assigneePublicId, ticketPublicId);
        return ResponseEntity.ok().body(getResponse(
                null,
                String.format("Assignee of ticket %s updated successfully", ticketPublicId),
                HttpStatus.OK
        ));
    }

    @PostMapping("/comment/")
    ResponseEntity<ApiResponse<UUID>> createComment(@NotNull Authentication authentication,
                                                    @RequestBody CreateCommentDTO createCommentDTO) {
        UUID comment = ticketService.createComment(UUID.fromString(authentication.getName()), createCommentDTO);
        return ResponseEntity.created(getUri()).body(getResponse(
                comment,
                "Comment added successfully.",
                HttpStatus.CREATED
        ));
    }

    @PatchMapping("/comment/update")
    public ResponseEntity<ApiResponse<Void>> updateComment(@NotNull Authentication authentication,
                                                           @RequestBody @Valid UpdateCommentDTO updateCommentDTO) {
        this.ticketService.updateComment(UUID.fromString(authentication.getName()), updateCommentDTO);
        return ResponseEntity.ok().body(getResponse(
                null,
                "Comment updated successfully.",
                HttpStatus.OK
        ));
    }

    @DeleteMapping("/comment/delete")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@NotNull Authentication authentication,
                                                           @RequestParam("commentPublicId") UUID commentPublicId) {
        this.ticketService.deleteComment(UUID.fromString(authentication.getName()), commentPublicId);
        return ResponseEntity.ok().body(getResponse(
                null,
                "Comment deleted successfully.",
                HttpStatus.OK
        ));
    }

    @PutMapping("/task/")
    ResponseEntity<ApiResponse<UUID>> createTask(@NotNull Authentication authentication,
                                                 @RequestBody CreateTaskDTO createTaskDTO) {
        UUID task = ticketService.createTask(UUID.fromString(authentication.getName()), createTaskDTO);
        return ResponseEntity.created(getUri()).body(getResponse(
                task,
                "Task added successfully.",
                HttpStatus.CREATED
        ));
    }

    @PutMapping("/task/update")
    public ResponseEntity<ApiResponse<Void>> updateTask(@NotNull Authentication authentication,
                                                        @RequestBody @Valid UpdateTaskDTO updateTaskDTO) {
        this.ticketService.updateTask(UUID.fromString(authentication.getName()), updateTaskDTO);
        return ResponseEntity.ok().body(getResponse(
                null,
                "Task updated successfully.",
                HttpStatus.OK
        ));
    }

    @DeleteMapping("/task/delete")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@NotNull Authentication authentication,
                                                        @RequestParam("taskPublicId") UUID taskPublicId) {
        this.ticketService.deleteTask(UUID.fromString(authentication.getName()), taskPublicId);
        return ResponseEntity.ok().body(getResponse(
                null,
                "Task deleted successfully.",
                HttpStatus.OK
        ));
    }

    @PostMapping("/file/upload")
    ResponseEntity<ApiResponse<UUID>> uploadFile(@NotNull Authentication authentication,
                                                 @RequestParam("ticketPublicId") UUID ticketPublicId,
                                                 @RequestParam("files") List<MultipartFile> files) {
        ticketService.uploadFiles(UUID.fromString(authentication.getName()), ticketPublicId, files);
        return ResponseEntity.created(getUri()).body(getResponse(
                null,
                "Files uploaded successfully.",
                HttpStatus.CREATED
        ));
    }

    private URI getUri() {
        return URI.create("/ticket/<ticketPublicId>");
    }

}
