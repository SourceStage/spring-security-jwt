package com.example.spring_security_jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_security_jwt.dto.ApiResponse;
import com.example.spring_security_jwt.dto.AuthenticationResponse;
import com.example.spring_security_jwt.dto.UserRequest;
import com.example.spring_security_jwt.service.AuthenticationService;
import com.example.spring_security_jwt.util.JWTUtil;

@RestController
@RequestMapping("/api")
public class JwtController {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private JWTUtil jwtUtil;

	@PostMapping("/login")
	ApiResponse<AuthenticationResponse> login(@RequestBody UserRequest userRequest) {
		var respsone = new ApiResponse<AuthenticationResponse>();
		respsone.setBody(authenticationService.authenticate(userRequest));
		return respsone;
	}

	@PostMapping("/introspect")
	ApiResponse<AuthenticationResponse> checkToken(@RequestBody UserRequest userRequest) {
		var respsone = new ApiResponse<AuthenticationResponse>();
		var body = new AuthenticationResponse();
		body.setIsTokenValid(jwtUtil.verifyToken(userRequest.getToken()));
		respsone.setBody(body);
		return respsone;
	}
}
