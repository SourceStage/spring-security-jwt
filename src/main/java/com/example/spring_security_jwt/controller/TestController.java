package com.example.spring_security_jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.spring_security_jwt.dto.TestResponse;
import com.example.spring_security_jwt.service.JwtService;
import com.example.spring_security_jwt.util.RestControllerUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController extends BaseController {

  private final JwtService jwtService;

  @GetMapping("public/test")
  public String test() {
    return "Test response";
  }

  @GetMapping("public/access-token")
  public ResponseEntity<Object> gernerateAccessToken() {
    UserDetails mockUSer = User.builder().username("vinh.nt").password("test").build();

    String accessToken = jwtService.generateAccessToken(mockUSer);

    return RestControllerUtil.successResponse(TestResponse.builder().accessToken(accessToken).build());
  }

}
