package com.example.spring_security_jwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Configuration
//@ConfigurationProperties(prefix = "spring.security.jwt")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class JwtProperty {

	@Value("${spring.security.jwt.signerKey}")
	String secret;

	@Value("${spring.security.jwt.issue}")
	String issue;

	@Value("${spring.security.jwt.valid-duration}")
	Long expirationTime;

	@Value("${spring.security.jwt.refreshable-duration}")
	Long refreshableDuration;
}
