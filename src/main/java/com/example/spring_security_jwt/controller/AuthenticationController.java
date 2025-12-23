package com.example.spring_security_jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.spring_security_jwt.dto.AuthenticationRequest;
import com.example.spring_security_jwt.dto.AuthenticationResponse;
import com.example.spring_security_jwt.dto.UserRequest;
import com.example.spring_security_jwt.service.AuthenticationService;
import com.example.spring_security_jwt.util.RestControllerUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController extends BaseController {

  private final AuthenticationService authenticationService;

  @PostMapping("auth/login")
  public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) {
    AuthenticationResponse response = authenticationService.authenticate(request);
    return RestControllerUtil.successResponse(response);
  }

  @PostMapping("auth/register")
  public ResponseEntity<Object> registerUser(@RequestBody UserRequest request) {
    var response = authenticationService.register(request);
    return RestControllerUtil.successResponse(response);
  }
}
