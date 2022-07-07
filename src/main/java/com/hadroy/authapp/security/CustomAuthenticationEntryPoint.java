package com.hadroy.authapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hadroy.authapp.model.response.ResponseError;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON_VALUE);
        ResponseError responseError = new ResponseError(
                UNAUTHORIZED.value(),
                UNAUTHORIZED.name(),
                List.of(authException.getMessage())
        );

        new ObjectMapper().writeValue(
                response.getOutputStream(),
                responseError
        );
    }
}
