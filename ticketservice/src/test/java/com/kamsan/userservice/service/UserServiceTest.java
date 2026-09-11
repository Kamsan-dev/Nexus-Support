package com.kamsan.userservice.service;

import com.kamsan.userservice.mapper.UserMapper;
import com.kamsan.userservice.repository.UserRepository;
import com.kamsan.userservice.repository.projection.UserRoleAndAuthoritiesProjection;
import com.kamsan.userservice.service.implementation.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    @InjectMocks
    TicketServiceImpl userService;

    @Test
    void getUserByEmail_shouldReturnDto_whenUserExists() {
        // Given
        String email = "john.doe@example.com";
        UUID publicId = UUID.randomUUID();

        User user = new User();
        user.setUserPublicId(publicId);
        user.setEmail(email);

        var projection = mock(UserRoleAndAuthoritiesProjection.class);
        when(projection.getRole()).thenReturn("USER");
        when(projection.getAuthorities()).thenReturn(
                "user:read,user:update,ticket:create,ticket:read,ticket:update,comment:create,comment:read,comment:update,comment:delete,task:read");

        ReadUserDTO expectedDto = mock(ReadUserDTO.class); // ← pas besoin des 19 args

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.findRoleAndAuthorities(publicId)).thenReturn(projection);
        when(userMapper.userToReadUserDTO(any(User.class))).thenReturn(expectedDto);

        // When
        ReadUserDTO result = userService.getUserByEmail(email);
        // Then
        assertThat(result).isSameAs(expectedDto);
        assertThat(user.getRole()).isEqualTo("USER");
        assertThat(user.getAuthorities()).contains(
                "user:read,user:update,ticket:create,ticket:read,ticket:update,comment:create,comment:read,comment:update,comment:delete,task:read");

        verify(userRepository).findByEmail(email);
        verify(userRepository).findRoleAndAuthorities(publicId);
        verify(userMapper).userToReadUserDTO(user);
    }

    @Test
    void getUserByEmail_shouldThrow_whenUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByEmail("unknown@example.com"))
                .isInstanceOf(UsernameNotFoundException.class);

        verify(userRepository).findByEmail("unknown@example.com");
        verifyNoInteractions(userMapper);
    }

}