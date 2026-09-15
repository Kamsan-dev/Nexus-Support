package com.kamsan.userservice.service;

import com.kamsan.userservice.dto.ReadUserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    ReadUserDTO getUserByUUID(UUID userPublicId);

    ReadUserDTO getAssignee(UUID ticketPublicId);

    List<ReadUserDTO> getTechSupports();

}
