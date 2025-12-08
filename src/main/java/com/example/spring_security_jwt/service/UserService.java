package com.example.spring_security_jwt.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.spring_security_jwt.dto.UserRequest;
import com.example.spring_security_jwt.entity.User;
import com.example.spring_security_jwt.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User createUser(UserRequest userRequest) {
		var user = new User();

		user.setUsername(userRequest.getUsername());
		user.setEmail(userRequest.getEmail());
		user.setPassword(userRequest.getPassword());
		user.setFullName(userRequest.getFullName());

		return userRepository.save(user);
	}

	public User updateUser(Long userId, UserRequest userRequest) {
		User userFound = getUserById(userId);

		userFound.setUsername(userRequest.getUsername());
		userFound.setEmail(userRequest.getEmail());
		userFound.setPassword(userRequest.getPassword());
		userFound.setFullName(userRequest.getFullName());

		return userRepository.save(userFound);
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
	}

	public List<User> getUsers() {
		return userRepository.findAll();
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}
