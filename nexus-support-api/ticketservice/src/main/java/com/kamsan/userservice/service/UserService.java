package com.kamsan.userservice.service;

import com.kamsan.userservice.dto.ReadUserDTO;
import com.kamsan.userservice.dto.TicketUserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    ReadUserDTO getUserByUUID(UUID userPublicId);

    TicketUserDTO getAssignee(UUID ticketPublicId);

    List<TicketUserDTO> getTechSupports();

}
