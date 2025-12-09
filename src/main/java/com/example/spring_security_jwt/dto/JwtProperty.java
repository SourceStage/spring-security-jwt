package com.example.spring_security_jwt.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtProperty {
	
	String secret;
	Long validDuration;

}
