package com.example.spring_security_jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.spring_security_jwt.constant.AppUrlConstants;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private static final String[] PUBLIC_URL =
      {AppUrlConstants.BASE_URL + "/public/**", AppUrlConstants.BASE_URL + "/auth/**"};

  private final AuthenticationProvider authenticationProvider;
  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationEntryPoint authenticationEntryPoint;
  private final ExceptionHandlerFilter exceptionHandlerFilter;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    // @formatter:off
    httpSecurity.securityMatcher(AppUrlConstants.BASE_URL + "/**");
    
    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    httpSecurity.authorizeHttpRequests(
        request -> request.requestMatchers(PUBLIC_URL).permitAll()
                          .anyRequest().authenticated());
    
    httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    

    httpSecurity.authenticationProvider(authenticationProvider);
    
    httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    
    httpSecurity.addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);

    httpSecurity.exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint));
    // @formatter:on
    return httpSecurity.build();
  }
}
