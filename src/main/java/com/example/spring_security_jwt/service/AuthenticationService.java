package com.example.spring_security_jwt.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring_security_jwt.dto.AuthenticationResponse;
import com.example.spring_security_jwt.dto.UserAuthenticated;
import com.example.spring_security_jwt.dto.UserRequest;
import com.example.spring_security_jwt.entity.InvalidTokenEntity;
import com.example.spring_security_jwt.entity.PermissionEntity;
import com.example.spring_security_jwt.entity.UserEntity;
import com.example.spring_security_jwt.exception.AuthenticationException;
import com.example.spring_security_jwt.repository.InvalidTokenRepository;
import com.example.spring_security_jwt.util.JWTUtil;
import com.nimbusds.jwt.SignedJWT;

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

	InvalidTokenRepository invalidTokenRepository;

	public AuthenticationResponse authenticate(UserRequest userLogin) {
		var authenticationResponse = new AuthenticationResponse();

		UserEntity userFound = userService.getUserByUsername(userLogin.getUsername());

		boolean isPasswordExactly = passwordEncoder.matches(userLogin.getPassword(), userFound.getPassword());

		if (isPasswordExactly) {
			List<SimpleGrantedAuthority> authorities = convertRoleForUserAuthenticated(userFound);

			var userAuthenticated = UserAuthenticated.createUserErasePassword(userFound.getUsername(), authorities);
			var token = jwtUtil.generateToken(userAuthenticated);
			authenticationResponse.setToken(token);
			authenticationResponse.setAuthenticated(true);
		} else {
			authenticationResponse.setAuthenticated(false);
		}

		return authenticationResponse;
	}

	private List<SimpleGrantedAuthority> convertRoleForUserAuthenticated(UserEntity userAuthenticated) {
		List<String> roles = userAuthenticated.getRoles().stream()
				.flatMap(role -> Stream.concat(Stream.of(role.getRoleName()),
						role.getPermissions().stream().map(PermissionEntity::getPermissionName)))
				.distinct().toList();

		return roles.stream().map(SimpleGrantedAuthority::new).toList();
	}

	public void logout(String token) {
		try {
			SignedJWT signedJWT = jwtUtil.verifyToken(token, true);

			String jit = signedJWT.getJWTClaimsSet().getJWTID();
			Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

			InvalidTokenEntity invalidToken = InvalidTokenEntity.builder().id(jit).expiresAt(expiryTime).build();

			invalidTokenRepository.save(invalidToken);

		} catch (Exception e) {
			log.info("Token already expired!");
		}
	}

	public AuthenticationResponse refreshToken(String token) {
		SignedJWT signedJWT = jwtUtil.verifyToken(token, true);

		logout(token);

		var username = jwtUtil.getSubjectFromToken(signedJWT);

		UserEntity userFound = userService.getUserByUsername(username);

		List<SimpleGrantedAuthority> authorities = convertRoleForUserAuthenticated(userFound);

		var userAuthenticated = UserAuthenticated.createUserErasePassword(userFound.getUsername(), authorities);
		var newToken = jwtUtil.generateToken(userAuthenticated);

		return AuthenticationResponse.builder().token(newToken).isAuthenticated(true).build();

	}

	public boolean introspect(String token) {
		boolean isValid = true;

		try {
			jwtUtil.verifyToken(token, false);
		} catch (AuthenticationException e) {
			isValid = false;
		}

		return isValid;
	}
}
