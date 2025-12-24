package com.example.spring_security_jwt.dto;

import com.example.spring_security_jwt.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

  private String email;
  private String password;
  private String firstname;
  private String lastname;
  private Role role;
}
