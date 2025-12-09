package com.example.spring_security_jwt.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordEncoderTest {

	@Test
	void md5Hash() throws NoSuchAlgorithmException {
		String rawPassword = "12345";

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(rawPassword.getBytes());

		byte[] digest = md.digest();
		String md5PasswordRoun1 = DatatypeConverter.printHexBinary(digest);
		log.info("MD5 round 1: {}", md5PasswordRoun1);

		md.update(rawPassword.getBytes());
		digest = md.digest();
		String md5PasswordRoun2 = DatatypeConverter.printHexBinary(digest);

		log.info("MD5 round 2: {}", md5PasswordRoun2);

		assertEquals(md5PasswordRoun1, md5PasswordRoun2);
	}

	@Test
	void bcrypt() throws NoSuchAlgorithmException {
		String rawPassword = "12345";

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

		String encodePasswordRound1 = passwordEncoder.encode(rawPassword);
		String encodePasswordRound2 = passwordEncoder.encode(rawPassword);

		log.info("BCrypt round 1: {}", encodePasswordRound1);
		log.info("BCrypt round 2: {}", encodePasswordRound2);

		assertNotEquals(encodePasswordRound1, encodePasswordRound2);
	}
}
