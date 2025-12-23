package com.example.spring_security_jwt.config;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.example.spring_security_jwt.dto.ErrorDto;
import com.example.spring_security_jwt.util.RestControllerUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

    var errorDetail = ErrorDto.builder().message(authException.getMessage()).build();

    RestControllerUtil.sendError(response, HttpStatus.UNAUTHORIZED.value(), errorDetail);
  }

}
