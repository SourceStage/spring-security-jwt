package com.example.spring_security_jwt.dto;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserAuthenticated extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4282531512653525294L;

	public UserAuthenticated(String username, @Nullable String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public UserAuthenticated(String username, @Nullable String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
}
