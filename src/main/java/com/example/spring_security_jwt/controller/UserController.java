package com.example.spring_security_jwt.controller;

import java.util.List;

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
import com.example.spring_security_jwt.entity.User;
import com.example.spring_security_jwt.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@PostMapping
	User createUser(@Valid @RequestBody UserRequest request) {
		return userService.createUser(request);
	}

	@GetMapping
	ApiResponse<List<User>> getUsers() {
		var response = new ApiResponse<List<User>>();
		response.setBody(userService.getUsers());
		return response;
	}

	@GetMapping("/{userId}")
	User getUser(@PathVariable Long userId) {
		return userService.getUserById(userId);
	}

	@PutMapping("/{userId}")
	User updateUser(@PathVariable Long userId, @RequestBody UserRequest request) {
		return userService.updateUser(userId, request);
	}

	@DeleteMapping("/{userId}")
	String deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return "User has been deleted";
	}
}
