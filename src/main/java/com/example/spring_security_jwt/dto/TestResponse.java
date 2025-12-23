package com.example.spring_security_jwt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestResponse implements ResponseDto {

  private Object accessToken;
}
