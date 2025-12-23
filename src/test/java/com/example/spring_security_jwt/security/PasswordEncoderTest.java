package com.example.spring_security_jwt.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
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

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    String encodePasswordRound1 = passwordEncoder.encode(rawPassword);
    String encodePasswordRound2 = passwordEncoder.encode(rawPassword);

    log.info("BCrypt round 1: {}", encodePasswordRound1);
    log.info("BCrypt round 2: {}", encodePasswordRound2);

    assertNotEquals(encodePasswordRound1, encodePasswordRound2);
  }

  @Test
  void generatebase64Key() {
    SecretKey key = Jwts.SIG.HS256.key().build();
    String base64Key = Encoders.BASE64.encode(key.getEncoded());

    log.info("Secret Key HS256 (256 bit): {}", base64Key);
    assertNotNull(base64Key);
  }
}
