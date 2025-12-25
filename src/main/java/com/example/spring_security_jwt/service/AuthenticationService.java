package com.example.spring_security_jwt.service;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.spring_security_jwt.constant.TokenType;
import com.example.spring_security_jwt.dto.AuthenticationRequest;
import com.example.spring_security_jwt.dto.AuthenticationResponse;
import com.example.spring_security_jwt.dto.DataOperationResponse;
import com.example.spring_security_jwt.dto.UserRequest;
import com.example.spring_security_jwt.entity.Token;
import com.example.spring_security_jwt.entity.User;
import com.example.spring_security_jwt.repository.TokenRepository;
import com.example.spring_security_jwt.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;

  /**
   * Authenticate user and generate JWT tokens
   * 
   * @param request
   * @return
   */
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    var userLogin = UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(),
        request.getPassword());
    authenticationManager.authenticate(userLogin);

    var userInfo = userRepository.findByEmail(request.getEmail()).orElseThrow();

    var accessToken = jwtService.generateAccessToken(userInfo);
    var refreshToken = jwtService.generateRefreshToken(userInfo);

    revokeAllUserTokens(userInfo);

    saveUserToken(userInfo, accessToken);

    return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
        .build();
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

    if (validUserTokens.isEmpty())
      return;

    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });

    tokenRepository.saveAll(validUserTokens);
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER)
        .expired(false).revoked(false).build();

    tokenRepository.save(token);
  }

  /**
   * Register new user
   * 
   * @param userRequest
   * @return
   */
  public DataOperationResponse register(UserRequest userRequest) {
    String passwordEncode = passwordEncoder.encode(userRequest.getPassword());

    var user = User.builder().firstname(userRequest.getFirstname())
        .lastname(userRequest.getLastname()).email(userRequest.getEmail()).password(passwordEncode)
        .role(userRequest.getRole()).build();

    userRepository.save(user);

    return new DataOperationResponse();
  }

  /**
   * Generate new access token using refresh token
   * 
   * @param request
   * @param response
   * @throws IOException
   */
  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }

    final String refreshToken = authHeader.substring(7);
    final String userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = userRepository.findByEmail(userEmail).orElseThrow();

      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateAccessToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder().accessToken(accessToken)
            .refreshToken(refreshToken).build();

        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
