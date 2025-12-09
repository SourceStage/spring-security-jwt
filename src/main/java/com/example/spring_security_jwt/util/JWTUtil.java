package com.example.spring_security_jwt.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import com.example.spring_security_jwt.dto.JwtProperty;
import com.example.spring_security_jwt.dto.UserAuthenticated;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class JWTUtil {

	public static String generateToken(UserAuthenticated user, JwtProperty property) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject(user.getUsername()).issuer("devteria.com")
				.issueTime(new Date())
				.expirationTime(
						new Date(Instant.now().plus(property.getValidDuration(), ChronoUnit.SECONDS).toEpochMilli()))
				.jwtID(UUID.randomUUID().toString()).build();

		Payload payload = new Payload(jwtClaimsSet.toJSONObject());

		JWSObject jwsObject = new JWSObject(header, payload);

		try {
			jwsObject.sign(new MACSigner(property.getSecret().getBytes()));
			return jwsObject.serialize();
		} catch (JOSEException e) {
			log.error("Cannot create token", e);
			throw new RuntimeException(e);
		}
	}
}
