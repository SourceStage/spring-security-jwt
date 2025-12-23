package com.example.spring_security_jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataOperationResponse implements ResponseDto {

  @Builder.Default
  private String message = "Persist Data Success!";
}
