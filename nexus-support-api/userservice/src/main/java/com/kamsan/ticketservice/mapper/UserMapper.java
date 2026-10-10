package com.kamsan.ticketservice.mapper;

import com.kamsan.ticketservice.dto.CreateUserDTO;
import com.kamsan.ticketservice.dto.CredentialDTO;
import com.kamsan.ticketservice.dto.ReadUserDTO;
import com.kamsan.ticketservice.dto.UpdateUserDTO;
import com.kamsan.ticketservice.model.Credential;
import com.kamsan.ticketservice.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    ReadUserDTO userToReadUserDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UpdateUserDTO updateUserDTO, @MappingTarget User user);

    User createUserDTOToUser(CreateUserDTO createUserDTO);

    CredentialDTO credentialToCredentialDTO(Credential credential);

}