package com.example.spring_security_jwt.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.spring_security_jwt.dto.ApiResponse;
import com.example.spring_security_jwt.dto.ErrorCode;

@ControllerAdvice
public class GlobalExceptionHandle {

	@ExceptionHandler(value = LogicException.class)
	ResponseEntity<ApiResponse<String>> handlingRuntimeException(LogicException exception) {
		var errorCode = exception.getErrorCode();
		var response = new ApiResponse<String>();
		response.setCode(errorCode.getCode());
		response.setBody(errorCode.getMessage());

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	ResponseEntity<ApiResponse<String>> handlingMethodArgumentNotValidException(
			MethodArgumentNotValidException exception) {
		String errorsEnumKey = exception.getFieldError().getDefaultMessage();

		ErrorCode errorCode = ErrorCode.INVALID_KEY_VALIDATE;

		try {
			errorCode = ErrorCode.valueOf(errorsEnumKey);
		} catch (IllegalArgumentException e) {
		}

		var response = new ApiResponse<String>();
		response.setCode(errorCode.getCode());
		response.setBody(errorCode.getMessage());

		return ResponseEntity.badRequest().body(response);
	}
}
