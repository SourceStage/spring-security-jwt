package com.example.spring_security_jwt.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_security_jwt.dto.ApiResponse;
import com.example.spring_security_jwt.dto.UserRequest;
import com.example.spring_security_jwt.entity.UserEntity;
import com.example.spring_security_jwt.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

	private final UserService userService;

	@PostMapping
	UserEntity createUser(@Valid @RequestBody UserRequest request) {
		return userService.createUser(request);
	}

	@GetMapping
	ApiResponse<List<UserEntity>> getUsers() {
		var response = new ApiResponse<List<UserEntity>>();
		response.setBody(userService.getUsers());
		return response;
	}

	@GetMapping("/{userId}")
	UserEntity getUser(@PathVariable Long userId) {
		var context = SecurityContextHolder.getContext();
		var authority = context.getAuthentication().getAuthorities();
		log.info("Roles: {}", authority);
		return userService.getUserById(userId);
	}

	@PutMapping("/{userId}")
	UserEntity updateUser(@PathVariable Long userId, @RequestBody UserRequest request) {
		return userService.updateUser(userId, request);
	}

	@DeleteMapping("/{userId}")
	String deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return "User has been deleted";
	}
}
