package com.example.spring_security_jwt.service;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.spring_security_jwt.dto.ErrorCode;
import com.example.spring_security_jwt.dto.UserRequest;
import com.example.spring_security_jwt.entity.RoleEntity;
import com.example.spring_security_jwt.entity.UserEntity;
import com.example.spring_security_jwt.exception.AuthenticationException;
import com.example.spring_security_jwt.exception.LogicException;
import com.example.spring_security_jwt.repository.RoleRepository;
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
	RoleRepository roleRepository;

	public UserEntity createUser(UserRequest userRequest) {
		var user = new UserEntity();

		user.setUsername(userRequest.getUsername());
		user.setEmail(userRequest.getEmail());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setFullName(userRequest.getFullName());

		var roles = new HashSet<RoleEntity>();

		if (!CollectionUtils.isEmpty(userRequest.getRoles())) {
			for (var role : userRequest.getRoles()) {
				RoleEntity roleEntity = roleRepository.findByRoleName(role).stream().findFirst().orElse(null);
				if (ObjectUtils.isNotEmpty(roleEntity)) {
					roles.add(roleEntity);
				}
			}
		}

		user.setRoles(roles);

		return userRepository.save(user);
	}

	public UserEntity updateUser(Long userId, UserRequest userRequest) {
		UserEntity userFound = getUserById(userId);

		userFound.setUsername(userRequest.getUsername());
		userFound.setEmail(userRequest.getEmail());
		userFound.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		userFound.setFullName(userRequest.getFullName());

		return userRepository.save(userFound);
	}

	@PreAuthorize("hasRole('USER')")
	public UserEntity getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new LogicException(ErrorCode.USER_EXISTED));
	}

	public List<UserEntity> getUsers() {
		return userRepository.findAll();
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	public UserEntity getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new AuthenticationException(ErrorCode.UN_AUTHENTICATION));
	}
}
