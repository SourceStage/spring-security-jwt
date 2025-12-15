package com.example.spring_security_jwt.util;

import org.springframework.http.HttpHeaders;

import com.example.spring_security_jwt.dto.ErrorCode;
import com.example.spring_security_jwt.exception.AuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestUtil {

	public static String getBearerToken(HttpServletRequest request) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new AuthenticationException(ErrorCode.UN_AUTHENTICATION);
		}

		String token = authHeader.substring(7).trim();

		if (token.isEmpty()) {
			throw new AuthenticationException(ErrorCode.UN_AUTHENTICATION);
		}

		return token;
	}
}
