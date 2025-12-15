package com.example.spring_security_jwt.config;

import java.util.Objects;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.example.spring_security_jwt.util.JWTUtil;

@Component
public class ApiJwtDecoder implements JwtDecoder {

	@Autowired
	private JwtProperty jwtProperty;

	@Autowired
	private JWTUtil jwtUtil;

	private NimbusJwtDecoder nimbusJwtDecoder = null;

	@Override
	public Jwt decode(String token) throws JwtException {
		try {
			jwtUtil.verifyToken(token, false);
		} catch (Exception e) {
			throw new JwtException("Token invalid");
		}

		if (Objects.isNull(nimbusJwtDecoder)) {
			SecretKeySpec secretKeySpec = new SecretKeySpec(jwtProperty.getSecret().getBytes(), "HS512");
			nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
		}

		return nimbusJwtDecoder.decode(token);
	}

}
