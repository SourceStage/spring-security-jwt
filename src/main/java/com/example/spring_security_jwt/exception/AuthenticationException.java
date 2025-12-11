package com.example.spring_security_jwt.exception;

import com.example.spring_security_jwt.dto.ErrorCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationException extends ApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3213039911533560436L;

	public AuthenticationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
