package com.example.spring_security_jwt.config;

import java.io.IOException;
import java.util.Arrays;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.spring_security_jwt.repository.TokenRepository;
import com.example.spring_security_jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String[] IGNORE_URL = {"/api/v1/auth/login"};

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    log.info("JwtAuthenticationFilter started!");

    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.info("JwtAuthenticationFilter: Not found authentication info!");
      filterChain.doFilter(request, response);
      return;
    }

    final String jwtToken = authHeader.substring(7);
    final String userEmail = jwtService.extractUsername(jwtToken);

    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
      boolean isTokenValidExists = tokenRepository.findByToken(jwtToken)
          .map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);

      if (jwtService.isTokenValid(jwtToken, userDetails) && isTokenValidExists) {
        var authenticatedToken = UsernamePasswordAuthenticationToken.authenticated(userDetails,
            null, userDetails.getAuthorities());

        authenticatedToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
      }
    }

    log.info("JwtAuthenticationFilter ended!");
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String servletPath = request.getServletPath();
    return Arrays.asList(IGNORE_URL).contains(servletPath);
  }

}
