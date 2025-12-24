package com.example.spring_security_jwt.constant;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

  // @formatter:off
  USER(Collections.emptySet()),

  ADMIN(
      Set.of(
          Permission.ADMIN_READ,
          Permission.ADMIN_UPDATE,
          Permission.ADMIN_DELETE,
          Permission.ADMIN_CREATE,
          Permission.MANAGER_READ,
          Permission.MANAGER_UPDATE,
          Permission.MANAGER_DELETE,
          Permission.MANAGER_CREATE
      )
  ),

  MANAGER(
        Set.of(
            Permission.MANAGER_READ,
            Permission.MANAGER_UPDATE,
            Permission.MANAGER_DELETE,
            Permission.MANAGER_CREATE
        )
  )

  ;
  // @formatter:on

  @Getter
  private final Set<Permission> permissions;

  /**
   * Get authorities list from role permissions and role name.
   * 
   * @return List of SimpleGrantedAuthority
   */
  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions().stream()
        .map(permission -> new SimpleGrantedAuthority(permission.getName()))
        .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
