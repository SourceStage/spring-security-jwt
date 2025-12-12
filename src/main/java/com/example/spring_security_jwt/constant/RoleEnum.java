package com.example.spring_security_jwt.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum RoleEnum {
	ADMIN, USER,;

	private static final Map<String, RoleEnum> MAP = Arrays.stream(values())
			.collect(Collectors.toMap(RoleEnum::name, r -> r));

	public static boolean contains(String key) {
		return MAP.containsKey(key);
	}
}
