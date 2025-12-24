package com.example.spring_security_jwt.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionMessage {

  NOT_FOUND("400", "%s entity is not found!");

  @Getter
  private final String errorCode;

  @Getter
  private final String message;
}
