package com.example.spring_security_jwt.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring_security_jwt.dto.AuthenticationResponse;
import com.example.spring_security_jwt.dto.UserAuthenticated;
import com.example.spring_security_jwt.dto.UserRequest;
import com.example.spring_security_jwt.entity.User;
import com.example.spring_security_jwt.util.JWTUtil;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

	JWTUtil jwtUtil;

	UserService userService;

	PasswordEncoder passwordEncoder;

	public AuthenticationResponse authenticate(UserRequest userLogin) {
		var authenticationResponse = new AuthenticationResponse();

		User userFound = userService.getUserByUsername(userLogin.getUsername());

		boolean isPasswordExactly = passwordEncoder.matches(userLogin.getPassword(), userFound.getPassword());

		if (isPasswordExactly) {
			var userAuthenticated = UserAuthenticated.createUserErasePassword(userFound.getUsername(), List.of());
			var token = jwtUtil.generateToken(userAuthenticated);
			authenticationResponse.setToken(token);
			authenticationResponse.setAuthenticated(true);
		} else {
			authenticationResponse.setAuthenticated(false);
		}

		return authenticationResponse;
	}
}
