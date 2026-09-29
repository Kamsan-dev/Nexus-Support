package com.kamsan.userservice.mapper;

import com.kamsan.userservice.dto.AttachmentDTO;
import com.kamsan.userservice.dto.TicketDetailsDTO;
import com.kamsan.userservice.model.Attachment;
import com.kamsan.userservice.model.Ticket;
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