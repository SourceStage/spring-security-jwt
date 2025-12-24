package com.example.spring_security_jwt.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto implements ResponseDto {
  private String errorCode;
  private String message;
  private Map<String, String> errors;

  public ErrorResponseDto(String errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

  public ErrorResponseDto(String errorCode, String message, Map<String, String> errors) {
    this(errorCode, message);
    this.errors = errors;
  }
}
