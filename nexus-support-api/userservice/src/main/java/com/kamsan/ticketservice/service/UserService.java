package com.kamsan.ticketservice.service;

import com.kamsan.ticketservice.dto.*;
import com.kamsan.ticketservice.model.User;
import com.kamsan.ticketservice.repository.projection.UserSecurityProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {
    ReadUserDTO getUserByEmail(String email);

    ReadUserDTO getUserByPublicId(UUID publicId);

    UserSecurityProjection getUserSecurityData(UUID publicId);

    ReadUserDTO updateUser(UpdateUserDTO updateUserDTO, UUID userPublicId);

    UUID createUser(CreateUserDTO createUserDTO);

    void verifyAccount(String token);

    ReadUserDTO enableMfa(UUID userPublicId);

    ReadUserDTO disableMfa(UUID userPublicId);

    ReadUserDTO uploadPhoto(UUID userPublicId, MultipartFile file);

    void updatePassword(ChangePasswordDTO changePasswordDTO, UUID userPublicId);

    void resetPassword(String email);

    User verifyPasswordToken(String token);

    void doResetPassword(DoResetPasswordDTO doResetPasswordDTO);

    Page<PageUserDTO> getUsers(Pageable page);

    TicketUserDTO getAssignee(UUID ticketPublicId);

    CredentialDTO getCredential(UUID userPublicId);

    List<DeviceDTO> getDevices(UUID userPublicId);

    List<TicketUserDTO> getTechSupports();

    /**
     * Admin
     **/

    ReadUserDTO toggleAccountExpired(UUID userPublicId);

    ReadUserDTO toggleAccountLocked(UUID userPublicId);

    ReadUserDTO toggleAccountEnabled(UUID userPublicId);

    ReadUserDTO toggleCredentialsExpired(UUID userPublicId);

    ReadUserDTO updateRole(UUID userPublicId, UUID rolePublicId);

}
