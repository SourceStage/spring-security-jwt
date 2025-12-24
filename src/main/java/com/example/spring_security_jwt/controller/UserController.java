package com.example.spring_security_jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.example.spring_security_jwt.entity.User;
import com.example.spring_security_jwt.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {

  private final UserService userService;

  @GetMapping("user/{userId}")
  ResponseEntity<User> getUserInfo(@PathVariable Integer userId) {
    var user = userService.getUserById(userId);

    return ResponseEntity.ok(user);
  }
}
