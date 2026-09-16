package com.kamsan.userservice.security.handler;

import com.kamsan.userservice.sharedkernel.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
public class RestClientInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        try {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            var token = httpServletRequest.getHeader(AUTHORIZATION);
            request.getHeaders().set(AUTHORIZATION, token == null ? "" : token);
            return execution.execute(request, body);
        } catch (Exception ex) {
            log.error(String.format("Unable to intercept token : {}", ex.getMessage()));
            throw new ApiException("Unable to execute request");
        }
    }
}