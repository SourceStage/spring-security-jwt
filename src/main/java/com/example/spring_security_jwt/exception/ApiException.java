package com.example.spring_security_jwt.exception;

import com.example.spring_security_jwt.dto.ErrorCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ApiException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final ErrorCode errorCode;

	public ApiException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

}
