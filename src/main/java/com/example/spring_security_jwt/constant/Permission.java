package com.example.spring_security_jwt.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

  // @formatter:off
  ADMIN_READ("admin:read"),
  ADMIN_UPDATE("admin:update"),
  ADMIN_CREATE("admin:create"),
  ADMIN_DELETE("admin:delete"),
  MANAGER_READ("management:read"),
  MANAGER_UPDATE("management:update"),
  MANAGER_CREATE("management:create"),
  MANAGER_DELETE("management:delete")

  ;
  // @formatter:on

  @Getter
  private final String name;
}
