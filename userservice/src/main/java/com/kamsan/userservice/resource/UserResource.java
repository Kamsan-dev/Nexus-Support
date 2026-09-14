package com.kamsan.userservice.resource;

import com.kamsan.userservice.domain.ApiResponse;
import com.kamsan.userservice.domain.UserProperties;
import com.kamsan.userservice.dto.*;
import com.kamsan.userservice.service.implementation.UserServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static com.kamsan.userservice.constants.Constants.USER_UPDATED_SUCCESSFULLY;
import static com.kamsan.userservice.utils.RequestUtils.getResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserResource {

    private final UserServiceImpl userService;
    private final UserProperties userProperties;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid CreateUserDTO createUserDTO) {
        this.userService.createUser(createUserDTO);
        return ResponseEntity.created(getUri()).body(getResponse(
                null,
                "Account created. Check your email to enable your account.",
                HttpStatus.CREATED));
    }

    @GetMapping("/verify/account")
    public ResponseEntity<ApiResponse<Void>> verifyAccount(@RequestParam("token") String token) {
        userService.verifyAccount(token);
        return ResponseEntity.ok().body(getResponse(
                null,
                "Account verified. You may login now.",
                HttpStatus.OK));
    }

    @PatchMapping("/mfa/enable")
    public ResponseEntity<ApiResponse<ReadUserDTO>> enableMfa(@NotNull Authentication authentication) {
        authentication.get
        ReadUserDTO userDTO = userService.enableMfa(UUID.fromString(authentication.getName()));
        return ResponseEntity.ok().body(getResponse(
                userDTO,
                "MFA is now successfully enabled",
                HttpStatus.OK));
    }

    @PatchMapping("/mfa/disable")
    public ResponseEntity<ApiResponse<ReadUserDTO>> disableMfa(@NotNull Authentication authentication) {
        ReadUserDTO userDTO = userService.disableMfa(UUID.fromString(authentication.getName()));
        return ResponseEntity.ok().body(getResponse(
                userDTO,
                "MFA is now successfully disabled",
                HttpStatus.OK));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileDTO>> profile(@NotNull Authentication authentication) {
        ReadUserDTO user = userService.getUserByPublicId(UUID.fromString(authentication.getName()));
        List<DeviceDTO> devices = userService.getDevices(UUID.fromString(authentication.getName()));
        return ResponseEntity.ok().body(getResponse(
                new ProfileDTO(user, devices),
                null,
                HttpStatus.OK));
    }

    @GetMapping("/{userPublicId}")
    public ResponseEntity<ApiResponse<ReadUserDTO>> getUserByPublicId(@PathVariable("userPublicId") String userPublicId) {
        ReadUserDTO user = userService.getUserByPublicId(UUID.fromString(userPublicId));
        return ResponseEntity.ok().body(getResponse(
                user,
                null,
                HttpStatus.OK));
    }

    @GetMapping("/assignee/{ticketPublicId}")
    public ResponseEntity<ApiResponse<TicketUserDTO>> getAssigneeByTicketPublicId(@PathVariable("ticketPublicId") String ticketPublicId) {
        var assignee = userService.getAssignee(UUID.fromString(ticketPublicId));
        return ResponseEntity.ok().body(getResponse(
                assignee,
                null,
                HttpStatus.OK));
    }

    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<ReadUserDTO>> getUserByEmail(@PathVariable("email") String email) {
        ReadUserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok().body(getResponse(
                user,
                null,
                HttpStatus.OK));
    }

    @GetMapping("/{userPublicId}/credential")
    public ResponseEntity<ApiResponse<CredentialDTO>> getCredentials(@PathVariable("userPublicId") String userPublicId) {
        CredentialDTO credential = userService.getCredential(UUID.fromString(userPublicId));
        return ResponseEntity.ok().body(getResponse(
                credential,
                null,
                HttpStatus.OK));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ReadUserDTO>> updateUser(@NotNull Authentication authentication, @RequestBody UpdateUserDTO updateUserDTO) {
        ReadUserDTO user = userService.updateUser(updateUserDTO, UUID.fromString(authentication.getName()));
        return ResponseEntity.ok().body(getResponse(
                user,
                "Your profile has been updated successfully.",
                HttpStatus.OK));
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@NotNull Authentication authentication, @RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.updatePassword(changePasswordDTO, UUID.fromString(authentication.getName()));
        return ResponseEntity.ok().body(getResponse(
                null,
                "Your password has been updated successfully.",
                HttpStatus.OK));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestParam("email") @Email(message = "Invalid email address") String email) {
        this.userService.resetPassword(email);
        return ResponseEntity.ok().body(getResponse(
                null,
                "We sent you an email for you to reset your password.",
                HttpStatus.OK));
    }

    @GetMapping("/password/reset/verify")
    public ResponseEntity<ApiResponse<Void>> resetPasswordTokenVerify(@RequestParam("token") String token) {
        this.userService.verifyPasswordToken(token);
        return ResponseEntity.ok().body(getResponse(
                null,
                null,
                HttpStatus.OK));
    }

    @PostMapping("/password/reset/confirm")
    public ResponseEntity<ApiResponse<Void>> resetPasswordConfirm(@RequestBody DoResetPasswordDTO doResetPasswordDTO) {
        this.userService.doResetPassword(doResetPasswordDTO);
        return ResponseEntity.ok().body(getResponse(
                null,
                "Your password has been reset successfully. You may login now.",
                HttpStatus.OK));
    }

    @PatchMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ReadUserDTO>> updateProfilePhoto(@NotNull Authentication authentication, @RequestParam("file") MultipartFile file) {
        ReadUserDTO user = userService.uploadPhoto(UUID.fromString(authentication.getName()), file);
        return ResponseEntity.ok().body(getResponse(
                user,
                "Your profile image has been updated successfully.",
                HttpStatus.OK));
    }

    @GetMapping(value = "/profile-image/{filename}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getUserProfileImage(@PathVariable(name = "filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(userProperties.imagesFolder() + "/" + filename));
    }

    @GetMapping("get-all")
    public ResponseEntity<ApiResponse<Page<PageUserDTO>>> getPageUsers(Pageable pageable) {
        return ResponseEntity.ok().body(getResponse(
                userService.getUsers(pageable),
                null,
                HttpStatus.OK));
    }

    /**
     * ADMIN
     **/

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('SUPER_ADMIN'")
    @PutMapping("/{userPublicId}/role")
    public ResponseEntity<ApiResponse<ReadUserDTO>> updateRole(@NotNull Authentication authentication, @PathVariable("userPublicId") String userPublicId,
                                                               @RequestBody @NotNull @NotEmpty String rolePublicId) {
        ReadUserDTO user = userService.updateRole(UUID.fromString(userPublicId), UUID.fromString(rolePublicId));
        return ResponseEntity.ok().body(getResponse(
                user,
                USER_UPDATED_SUCCESSFULLY,
                HttpStatus.OK));
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('SUPER_ADMIN'")
    @PatchMapping("/{userPublicId}/account/expired")
    public ResponseEntity<ApiResponse<ReadUserDTO>> toggleAccountExpired(@NotNull Authentication authentication, @PathVariable("userPublicId") String userPublicId) {
        ReadUserDTO user = userService.toggleAccountExpired(UUID.fromString(userPublicId));
        return ResponseEntity.ok().body(getResponse(
                user,
                USER_UPDATED_SUCCESSFULLY,
                HttpStatus.OK));
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('SUPER_ADMIN'")
    @PatchMapping("/{userPublicId}/account/locked")
    public ResponseEntity<ApiResponse<ReadUserDTO>> toggleAccountLocked(@NotNull Authentication authentication, @PathVariable("userPublicId") String userPublicId) {
        ReadUserDTO user = userService.toggleAccountLocked(UUID.fromString(userPublicId));
        return ResponseEntity.ok().body(getResponse(
                user,
                USER_UPDATED_SUCCESSFULLY,
                HttpStatus.OK));
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('SUPER_ADMIN'")
    @PatchMapping("/{userPublicId}/account/enabled")
    public ResponseEntity<ApiResponse<ReadUserDTO>> toggleAccountEnabled(@NotNull Authentication authentication, @PathVariable("userPublicId") String userPublicId) {
        ReadUserDTO user = userService.toggleAccountEnabled(UUID.fromString(userPublicId));
        return ResponseEntity.ok().body(getResponse(
                user,
                USER_UPDATED_SUCCESSFULLY,
                HttpStatus.OK));
    }

    private URI getUri() {
        return URI.create("/profile/<userId>");
    }

}
