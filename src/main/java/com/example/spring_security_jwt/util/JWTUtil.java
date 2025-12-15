package com.example.spring_security_jwt.util;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.spring_security_jwt.config.JwtProperty;
import com.example.spring_security_jwt.constant.RoleEnum;
import com.example.spring_security_jwt.dto.ErrorCode;
import com.example.spring_security_jwt.dto.UserAuthenticated;
import com.example.spring_security_jwt.exception.AuthenticationException;
import com.example.spring_security_jwt.exception.JwtException;
import com.example.spring_security_jwt.repository.InvalidTokenRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Data
public final class JWTUtil {

	JwtProperty jwtProperty;

	InvalidTokenRepository invalidTokenRepository;

	public String generateToken(UserAuthenticated user) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

		var iss = jwtProperty.getIssue();
		var iat = new Date();
		var exp = new Date(Instant.now().plus(jwtProperty.getExpirationTime(), ChronoUnit.SECONDS).toEpochMilli());
		var jti = UUID.randomUUID().toString();

		// @formatter:off
		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				.subject(user.getUsername())
				.issuer(iss)
				.issueTime(iat)
				.expirationTime(exp)
				.claim("scope", buildScope(user))
				.jwtID(jti)
				.build();
		// @formatter:on

		Payload payload = new Payload(jwtClaimsSet.toJSONObject());

		JWSObject jwsObject = new JWSObject(header, payload);

		try {
			byte[] secret = jwtProperty.getSecret().getBytes();
			var macSigner = new MACSigner(secret);

			jwsObject.sign(macSigner);
			return jwsObject.serialize();
		} catch (JOSEException e) {
			throw new JwtException(ErrorCode.JWT_ERROR);
		}
	}

	public SignedJWT verifyToken(String token, boolean isRefresh) {
		try {
			SignedJWT signedJWT = verifySignedToken(token);

			checkTokenExpirationTime(signedJWT, isRefresh);

			checkTokenLoggedOut(signedJWT);

			return signedJWT;

		} catch (JOSEException | ParseException | JwtException | AuthenticationException e) {
			throw new AuthenticationException(ErrorCode.UN_AUTHENTICATION);
		}
	}

	public String getSubjectFromToken(SignedJWT signedJWT) {
		try {
			var claim = signedJWT.getJWTClaimsSet();
			return claim.getSubject();
		} catch (ParseException e) {
			throw new JwtException(ErrorCode.JWT_ERROR);
		}
	}

	private SignedJWT verifySignedToken(String token) throws JOSEException, ParseException {
		byte[] secret = jwtProperty.getSecret().getBytes();
		JWSVerifier verifier = new MACVerifier(secret);
		SignedJWT signedJWT = SignedJWT.parse(token);

		boolean verified = signedJWT.verify(verifier);

		if (!verified) {
			throw new JwtException(ErrorCode.JWT_ERROR);
		}

		return signedJWT;
	}

	private void checkTokenExpirationTime(SignedJWT signedJWT, boolean isRefresh) throws ParseException {
		Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

		if (isRefresh) {
			expiryTime = new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
					.plus(jwtProperty.getRefreshableDuration(), ChronoUnit.SECONDS).toEpochMilli());
		}

		Date currentDate = new Date();
		if (expiryTime.before(currentDate)) {
			throw new JwtException(ErrorCode.JWT_ERROR);
		}
	}

	private void checkTokenLoggedOut(SignedJWT signedJWT) throws ParseException {
		var jti = signedJWT.getJWTClaimsSet().getJWTID();

		if (invalidTokenRepository.existsById(jti)) {
			throw new AuthenticationException(ErrorCode.UN_AUTHENTICATION);
		}
	}

	private String buildScope(UserAuthenticated user) {
		var stringJoiner = new StringJoiner(" ");
		List<String> roles = user.getAuthorities().stream().map(item -> item.getAuthority()).toList();

		roles.forEach(roleName -> {
			if (RoleEnum.contains(roleName)) {
				String prefixRole = "ROLE_";
				stringJoiner.add(prefixRole + roleName);
			} else {
				stringJoiner.add(roleName);
			}
		});

		return stringJoiner.toString();
	}
}
