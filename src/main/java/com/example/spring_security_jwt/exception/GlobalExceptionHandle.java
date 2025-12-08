package com.example.spring_security_jwt.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandle {

	@ExceptionHandler(value = RuntimeException.class)
	ResponseEntity<String> handlingRuntimeException(RuntimeException exception) {
		return ResponseEntity.badRequest().body(exception.getMessage());
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	ResponseEntity<String> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		String errors = exception.getFieldError().getDefaultMessage();
		return ResponseEntity.badRequest().body(errors);
	}
}
