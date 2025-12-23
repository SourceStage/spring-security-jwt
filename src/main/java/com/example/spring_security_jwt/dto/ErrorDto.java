package com.example.spring_security_jwt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto implements ResponseDto {
  private String message;
}
