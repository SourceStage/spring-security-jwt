package com.example.spring_security_jwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Configuration
@ConfigurationProperties(prefix = "spring.security.jwt")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class JwtProperty {

	@Value("${signerKey}")
	String secret;

	@Value("${signerKey}")
	String issue;

	@Value("${valid-duration}")
	Long expirationTime;
}
