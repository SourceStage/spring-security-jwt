package com.example.spring_security_jwt.config;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.spring_security_jwt.dto.ErrorDto;
import com.example.spring_security_jwt.util.RestControllerUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (Exception e) {

      var errorDetail = ErrorDto.builder().message(e.getMessage()).build();

      RestControllerUtil.sendError(response, HttpStatus.UNAUTHORIZED.value(), errorDetail);
    }
  }
}
