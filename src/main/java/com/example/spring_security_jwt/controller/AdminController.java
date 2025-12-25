package com.example.spring_security_jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminController extends BaseController {

  @GetMapping("admin")
  @PreAuthorize("hasAuthority('admin:read')")
  public ResponseEntity<String> showDashboard() {
    return ResponseEntity.ok("AdminController: showDashboard");
  }
  
  @GetMapping("role/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> showDashboardAmin() {
    return ResponseEntity.ok("AdminController: showDashboard");
  }
}
