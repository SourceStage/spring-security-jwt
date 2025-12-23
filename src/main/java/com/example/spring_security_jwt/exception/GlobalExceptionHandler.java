package com.example.spring_security_jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.spring_security_jwt.dto.ErrorDto;
import com.example.spring_security_jwt.util.RestControllerUtil;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = AccessDeniedException.class)
  ResponseEntity<Object> handlingAccessDeniedException(AccessDeniedException exception) {

    return RestControllerUtil.errorResponse(
        ErrorDto.builder().message(exception.getMessage()).build(), HttpStatus.FORBIDDEN);
  }
}
