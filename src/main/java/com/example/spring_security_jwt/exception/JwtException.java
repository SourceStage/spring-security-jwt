package com.example.spring_security_jwt.exception;

import com.example.spring_security_jwt.dto.ErrorCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3213039911533560436L;

	private final ErrorCode errorCode;

	public JwtException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
