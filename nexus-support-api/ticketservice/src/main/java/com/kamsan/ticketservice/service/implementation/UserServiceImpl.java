package com.kamsan.ticketservice.service.implementation;

import com.kamsan.ticketservice.domain.ApiResponse;
import com.kamsan.ticketservice.dto.ReadUserDTO;
import com.kamsan.ticketservice.dto.TicketUserDTO;
import com.kamsan.ticketservice.security.handler.RestClientInterceptor;
import com.kamsan.ticketservice.service.UserService;
import com.kamsan.ticketservice.sharedkernel.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.UUID;

import static com.kamsan.ticketservice.utils.RequestUtils.convertResponse;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

        private final RestClient restClient;

        public UserServiceImpl( @Value("${userservice.uri}") String userServiceBaseUri, RestClientInterceptor interceptor) {
            log.info("user-service base uri : {}", userServiceBaseUri);
            this.restClient = RestClient.builder()
                                        .requestFactory(new HttpComponentsClientHttpRequestFactory())
                                        .baseUrl(userServiceBaseUri)
                                        .requestInterceptor(interceptor)
                                        .build();
        }

    @Override
    public ReadUserDTO getUserByUUID(UUID userPublicId) {
        var response = restClient.get()
                                 .uri(String.format("/user/%s", userPublicId))
                                 .retrieve()
                                 .body(ApiResponse.class);
        if (response != null) {
            return convertResponse(response, new TypeReference<>() {
            });
        } else throw new ApiException(String.format("Unable to retrieve user by public id %s", userPublicId));
    }

    @Override
    public TicketUserDTO getAssignee(UUID ticketPublicId) {
        var response = restClient.get()
                                 .uri(String.format("/user/assignee/%s", ticketPublicId))
                                 .retrieve()
                                 .body(ApiResponse.class);
        if (response != null) {
            return convertResponse(response, new TypeReference<>() {
            });
        } else throw new ApiException(String.format("Unable to retrieve assignee for ticket with public id %s",
                ticketPublicId));
    }

    @Override
    public List<TicketUserDTO> getTechSupports() {
        var response = restClient.get()
                                 .uri("/user/list/tech-supports")
                                 .retrieve()
                                 .body(ApiResponse.class);
        if (response != null) {
            return convertResponse(response, new TypeReference<>() {
            });
        } else throw new ApiException("Unable to retrieve the technician supports.");
    }
}
