package com.kamsan.ticketservice.mapper;

import com.kamsan.ticketservice.dto.AttachmentDTO;
import com.kamsan.ticketservice.dto.TicketDetailsDTO;
import com.kamsan.ticketservice.model.Attachment;
import com.kamsan.ticketservice.model.Ticket;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TicketDetailsDTO ticketToTicketDTO(Ticket ticket);

    AttachmentDTO attachmentToAttachmentDTO(Attachment attachment);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateUser(UpdateUserDTO updateUserDTO, @MappingTarget User user);
//
//    @Mapping(target = "password", ignore = true)
//    User createUserDTOToUser(CreateUserDTO createUserDTO);
//
//    CredentialDTO credentialToCredentialDTO(Credential credential);

}