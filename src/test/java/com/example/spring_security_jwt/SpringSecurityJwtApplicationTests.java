package com.example.spring_security_jwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class SpringSecurityJwtApplicationTests {

  @Test
  void contextLoads() {

    int number = 10;

    try {
      number = 2 / 0;
    } catch (RuntimeException e) {
      log.info("Exception: {}", number);
    }

    log.info("Value number: {}", number);
  }

}
