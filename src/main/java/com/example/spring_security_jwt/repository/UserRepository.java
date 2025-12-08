package com.example.spring_security_jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring_security_jwt.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
