package com.example.spring_security_jwt.config;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

	private static final String BASE_URL_API = "/api/**";
	private static final String[] PUBLIC_URL = new String[] { "/api/login", "/api/introspect" };

	private final JwtProperty jwtProperty;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
		// @formatter:off
		return httpSecurity
				.securityMatcher(BASE_URL_API)
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(rq -> rq.requestMatchers(PUBLIC_URL).permitAll()
						.anyRequest().authenticated())
				.oauth2ResourceServer(oauth2 -> 
					oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())
							.jwtAuthenticationConverter(jwtAuthenticationConverter()))
				)
				.build();
		// @formatter:on
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	JwtDecoder jwtDecoder() {
		var secretKeySpec = new SecretKeySpec(jwtProperty.getSecret().getBytes(), "HS512");
		return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
	}

	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

		return jwtAuthenticationConverter;

	}

}
