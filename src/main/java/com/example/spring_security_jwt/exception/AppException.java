package com.example.spring_security_jwt.exception;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public abstract class AppException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 2885048792644068859L;

  private final String errorCode;
  
  @JsonIgnore
  private final HttpStatus httpStatus;

  /**
   * 
   * @param errorCode
   * @param message
   * @param httpStatus
   */
  protected AppException(String errorCode, String message, HttpStatus httpStatus) {
    super(message);
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
  }

  /**
   * 
   * @param errorCode
   * @param message
   * @param httpStatus
   * @param cause
   */
  protected AppException(String errorCode, String message, HttpStatus httpStatus, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
  }
}
