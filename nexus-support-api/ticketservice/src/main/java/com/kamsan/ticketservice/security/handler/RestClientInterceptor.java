package com.kamsan.ticketservice.security.handler;

import com.kamsan.ticketservice.sharedkernel.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@Component
@Slf4j
public class RestClientInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution
    ) throws IOException {
        try {
            var attributes = (ServletRequestAttributes)
                    RequestContextHolder.currentRequestAttributes();

            HttpServletRequest incomingRequest = attributes.getRequest();
            String token = incomingRequest.getHeader(HttpHeaders.AUTHORIZATION);

            if (token != null && !token.isBlank()) {
                request.getHeaders().set(HttpHeaders.AUTHORIZATION, token);
            }

            log.debug("Forwarding request to user-service: {}", request.getURI());

        } catch (Exception ex) {
            log.error("Unable to retrieve the incoming request token", ex);
            throw new ApiException("Unable to retrieve incoming request token");
        }

        // Keep this outside the catch so you can distinguish token extraction
        // failures from downstream HTTP request failures.
        return execution.execute(request, body);
    }
}