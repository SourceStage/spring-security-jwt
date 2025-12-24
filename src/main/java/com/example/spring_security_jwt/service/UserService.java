package com.example.spring_security_jwt.service;

import org.springframework.stereotype.Service;
import com.example.spring_security_jwt.constant.ExceptionMessage;
import com.example.spring_security_jwt.entity.User;
import com.example.spring_security_jwt.exception.LogicException;
import com.example.spring_security_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User getUserById(Integer id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new LogicException(ExceptionMessage.NOT_FOUND, new String[] {"User"}));
  }
}
