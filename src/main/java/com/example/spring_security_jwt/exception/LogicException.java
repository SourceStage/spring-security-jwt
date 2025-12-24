package com.example.spring_security_jwt.exception;

import org.springframework.http.HttpStatus;
import com.example.spring_security_jwt.constant.ExceptionMessage;

public class LogicException extends AppException {

  /**
   * 
   */
  private static final long serialVersionUID = -2801433746721777053L;

  public LogicException(ExceptionMessage errorCode) {
    super(errorCode.getErrorCode(), errorCode.getMessage(), HttpStatus.BAD_REQUEST);
  }

  public LogicException(ExceptionMessage errorCode, String[] params) {
    super(errorCode.getErrorCode(), buildMessage(errorCode.getMessage(), params),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Helper method to build the message with parameters
   * 
   * @param template
   * @param params
   * @return
   */
  private static String buildMessage(String template, String[] params) {
    if (params == null || params.length == 0) {
      return template;
    }
    if (template != null && template.contains("%s")) {
      return String.format(template, (Object[]) params);
    }
    return template + " " + String.join(", ", params);
  }

}
