package com.kamsan.userservice.mapper;

import com.kamsan.userservice.dto.CreateUserDTO;
import com.kamsan.userservice.dto.CredentialDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    ReadUserDTO userToReadUserDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UpdateUserDTO updateUserDTO, @MappingTarget User user);

    @Mapping(target = "password", ignore = true)
    User createUserDTOToUser(CreateUserDTO createUserDTO);

    CredentialDTO credentialToCredentialDTO(Credential credential);

}