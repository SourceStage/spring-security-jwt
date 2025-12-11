package com.example.spring_security_jwt.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring_security_jwt.constant.RoleEnum;
import com.example.spring_security_jwt.dto.ErrorCode;
import com.example.spring_security_jwt.dto.UserRequest;
import com.example.spring_security_jwt.entity.User;
import com.example.spring_security_jwt.exception.AuthenticationException;
import com.example.spring_security_jwt.exception.LogicException;
import com.example.spring_security_jwt.repository.UserRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

	UserRepository userRepository;
	PasswordEncoder passwordEncoder;

	public User createUser(UserRequest userRequest) {
		var user = new User();

		user.setUsername(userRequest.getUsername());
		user.setEmail(userRequest.getEmail());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setFullName(userRequest.getFullName());

		var roles = new HashSet<String>();
		roles.add(RoleEnum.ADMIN.name());
		roles.add(RoleEnum.USER.name());
		user.setRoles(roles);

		return userRepository.save(user);
	}

	public User updateUser(Long userId, UserRequest userRequest) {
		User userFound = getUserById(userId);

		userFound.setUsername(userRequest.getUsername());
		userFound.setEmail(userRequest.getEmail());
		userFound.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		userFound.setFullName(userRequest.getFullName());

		return userRepository.save(userFound);
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new LogicException(ErrorCode.USER_EXISTED));
	}

	public List<User> getUsers() {
		return userRepository.findAll();
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username).stream().findFirst()
				.orElseThrow(() -> new AuthenticationException(ErrorCode.UN_AUTHENTICATION));
	}
}
