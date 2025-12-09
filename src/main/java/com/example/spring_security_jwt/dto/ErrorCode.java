package com.example.spring_security_jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	USER_EXISTED(404, "User existed"), INVALID_KEY_VALIDATE(400, "Request invalid!"),
	INVALID_USERNAME(400, "Username invalid!"),;

	private final int code;
	private final String message;

}
