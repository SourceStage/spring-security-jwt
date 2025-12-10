package com.example.spring_security_jwt.service;

import org.springframework.stereotype.Service;

import com.example.spring_security_jwt.util.JWTUtil;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

	JWTUtil jwtUtil;
}
