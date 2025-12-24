package com.example.spring_security_jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.spring_security_jwt.dto.ErrorResponseDto;
import com.example.spring_security_jwt.util.RestControllerUtil;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = AccessDeniedException.class)
  ResponseEntity<Object> handlingAccessDeniedException(AccessDeniedException exception) {

    return RestControllerUtil.errorResponse(
        ErrorResponseDto.builder().message(exception.getMessage()).build(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(value = LogicException.class)
  ResponseEntity<Object> handlingLogicException(LogicException exception) {

    var errorResponse = ErrorResponseDto.builder().errorCode(exception.getErrorCode())
        .message(exception.getMessage()).build();

    return RestControllerUtil.errorResponse(errorResponse, exception.getHttpStatus());
  }
}
