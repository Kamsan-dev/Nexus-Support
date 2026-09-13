package com.kamsan.userservice.mapper;

import com.kamsan.userservice.dto.TicketDTO;
import com.kamsan.userservice.model.Ticket;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TicketDTO ticketToTicketDTO(Ticket ticket);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateUser(UpdateUserDTO updateUserDTO, @MappingTarget User user);
//
//    @Mapping(target = "password", ignore = true)
//    User createUserDTOToUser(CreateUserDTO createUserDTO);
//
//    CredentialDTO credentialToCredentialDTO(Credential credential);

}