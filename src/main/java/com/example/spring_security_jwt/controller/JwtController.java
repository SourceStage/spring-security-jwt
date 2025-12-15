package com.example.spring_security_jwt.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_security_jwt.dto.ApiResponse;
import com.example.spring_security_jwt.dto.AuthenticationResponse;
import com.example.spring_security_jwt.dto.UserRequest;
import com.example.spring_security_jwt.service.AuthenticationService;
import com.example.spring_security_jwt.util.RestUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class JwtController {
	private final AuthenticationService authenticationService;

	@PostMapping("/login")
	ApiResponse<AuthenticationResponse> login(@RequestBody UserRequest userRequest) {
		var respsone = new ApiResponse<AuthenticationResponse>();
		respsone.setBody(authenticationService.authenticate(userRequest));
		return respsone;
	}

	@PostMapping("/logout")
	ApiResponse<Void> logout(HttpServletRequest request) {
		String token = RestUtil.getBearerToken(request);
		authenticationService.logout(token);
		return ApiResponse.<Void>builder().build();
	}

	@PostMapping("/introspect")
	ApiResponse<AuthenticationResponse> introspect(HttpServletRequest request) {
		var respsone = new ApiResponse<AuthenticationResponse>();
		var body = new AuthenticationResponse();
		String token = RestUtil.getBearerToken(request);
		body.setIsTokenValid(authenticationService.introspect(token));
		respsone.setBody(body);
		return respsone;
	}

	@PostMapping("/refresh")
	ApiResponse<AuthenticationResponse> refreshToken(HttpServletRequest request) {
		String token = RestUtil.getBearerToken(request);
		var result = authenticationService.refreshToken(token);
		return ApiResponse.<AuthenticationResponse>builder().body(result).build();
	}
}
