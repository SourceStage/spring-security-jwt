package com.example.spring_security_jwt.service;

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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    var userLogin = UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(),
        request.getPassword());
    authenticationManager.authenticate(userLogin);

    var userInfo = userRepository.findByEmail(request.getEmail()).orElseThrow();

    var accessToken = jwtService.generateAccessToken(userInfo);

    revokeAllUserTokens(userInfo);

    saveUserToken(userInfo, accessToken);

    return AuthenticationResponse.builder().accessToken(accessToken).build();
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

  public DataOperationResponse register(UserRequest userRequest) {
    String passwordEncode = passwordEncoder.encode(userRequest.getPassword());

    var user = User.builder().firstname(userRequest.getFirstname())
        .lastname(userRequest.getLastname()).email(userRequest.getEmail()).password(passwordEncode)
        .role(userRequest.getRole()).build();

    userRepository.save(user);

    return new DataOperationResponse();
  }
}
