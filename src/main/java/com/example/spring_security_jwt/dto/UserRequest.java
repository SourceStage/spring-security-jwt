package com.example.spring_security_jwt.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

	@Size(max = 10, message = "INVALID_USERNAME")
	private String username;
	private String email;
	private String password;
	private String fullName;
}
