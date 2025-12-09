package com.example.spring_security_jwt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(value = Include.NON_NULL)
public class ApiResponse<T> {
	int code = 200;
	String message;
	T body;
}
