package com.example.spring_security_jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private static final String BASE_URL_API = "/api/**";
	private static final String[] PUBLIC_URL = new String[] { "/api/login", "/api/introspect", "/api/refresh" };

	@Autowired
	private ApiJwtDecoder apiJwtDecoder;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
		// @formatter:off
		return httpSecurity
				.securityMatcher(BASE_URL_API)
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(rq -> rq.requestMatchers(PUBLIC_URL).permitAll()
						.anyRequest().authenticated())
				.oauth2ResourceServer(oauth2 -> 
					oauth2.jwt(jwt -> jwt.decoder(apiJwtDecoder)
							.jwtAuthenticationConverter(jwtAuthenticationConverter()))
						.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
				.build();
		// @formatter:on
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
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
