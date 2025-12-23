package com.example.spring_security_jwt.exception;

import org.springframework.security.core.AuthenticationException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtAuthenticationException extends AuthenticationException {

  /**
   * 
   */
  private static final long serialVersionUID = 8732421687177925994L;

  private final String message;

  public JwtAuthenticationException(String message) {
    super(message);
    this.message = message;
  }
}
