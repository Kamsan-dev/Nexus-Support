package com.kamsan.ticketservice.service;

import com.kamsan.ticketservice.dto.ReadUserDTO;
import com.kamsan.ticketservice.dto.TicketUserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    ReadUserDTO getUserByUUID(UUID userPublicId);

    TicketUserDTO getAssignee(UUID ticketPublicId);

    List<TicketUserDTO> getTechSupports();

}
