package com.hadroy.authapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hadroy.authapp.model.response.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("FORBIDDEN ACCESS for path [{}]", request.getServletPath());
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        ResponseError error = new ResponseError(
                FORBIDDEN.value(),
                FORBIDDEN.name(),
                List.of(accessDeniedException.getMessage())
        );
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
