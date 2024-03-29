package com.hadroy.authapp.controller;

import com.hadroy.authapp.error.NotFoundException;
import com.hadroy.authapp.error.UserRegisterException;
import com.hadroy.authapp.model.response.ResponseError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandleController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> notFoundHandler(NotFoundException notFoundException) {
        return new ResponseEntity<>(
                new ResponseError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), List.of(notFoundException.getMessage())),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(value = UserRegisterException.class)
    public ResponseEntity<Object> userRegisterExceptionHandler(UserRegisterException userRegisterException) {
        return new ResponseEntity<>(
                new ResponseError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), List.of(userRegisterException.getMessage())),
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return new ResponseEntity<>(
                new ResponseError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(
                new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(), List.of(ex.getMessage())),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
