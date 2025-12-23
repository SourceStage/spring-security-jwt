package com.example.spring_security_jwt.util;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.example.spring_security_jwt.dto.ErrorDto;
import com.example.spring_security_jwt.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestControllerUtil {

  public static ResponseEntity<Object> successResponse(ResponseDto body) {
    return makeJsonResponse(body, HttpStatus.OK);
  }

  public static ResponseEntity<Object> errorResponse(ResponseDto body, HttpStatus status) {
    return makeJsonResponse(body, status);
  }

  private static ResponseEntity<Object> makeJsonResponse(ResponseDto body, HttpStatus status) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    return new ResponseEntity<>(body, headers, status);
  }

  public static void sendError(HttpServletResponse response, int status, ErrorDto errordto)
      throws IOException {
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ObjectMapper objectMapper = new ObjectMapper();

    response.getWriter().write(objectMapper.writeValueAsString(errordto));
    response.flushBuffer();
  }
}
