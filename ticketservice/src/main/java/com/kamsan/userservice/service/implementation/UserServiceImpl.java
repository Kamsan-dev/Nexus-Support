package com.kamsan.userservice.service.implementation;

import com.kamsan.userservice.domain.ApiResponse;
import com.kamsan.userservice.dto.ReadUserDTO;
import com.kamsan.userservice.dto.TicketUserDTO;
import com.kamsan.userservice.security.handler.RestClientInterceptor;
import com.kamsan.userservice.service.UserService;
import com.kamsan.userservice.sharedkernel.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

import static com.kamsan.userservice.utils.RequestUtils.convertResponse;

@Service
public class UserServiceImpl implements UserService {

    private final RestClient restClient;
    @Value("${userservice.uri}")
    private String userServiceBaseUri;

    public UserServiceImpl() {
        this.restClient = RestClient.builder()
                                    .requestFactory(new HttpComponentsClientHttpRequestFactory())
                                    .baseUrl(this.userServiceBaseUri)
                                    .requestInterceptor(new RestClientInterceptor())
                                    .build();
    }

    @Override
    public ReadUserDTO getUserByUUID(UUID userPublicId) {
        var response = restClient.get()
                                 .uri(String.format("/user/%s", userPublicId))
                                 .retrieve()
                                 .body(ApiResponse.class);
        if (response != null) {
            return convertResponse(response, ReadUserDTO.class);
        } else throw new ApiException(String.format("Unable to retrieve user by public id %s", userPublicId));
    }

    @Override
    public TicketUserDTO getAssignee(UUID ticketPublicId) {
        var response = restClient.get()
                                 .uri(String.format("/user/assignee/%s", ticketPublicId))
                                 .retrieve()
                                 .body(ApiResponse.class);
        if (response != null) {
            return convertResponse(response, TicketUserDTO.class);
        } else throw new ApiException(String.format("Unable to retrieve assignee for ticket with public id %s",
                ticketPublicId));
    }

    @Override
    public List<ReadUserDTO> getTechSupports() {
        return List.of();
    }
}
