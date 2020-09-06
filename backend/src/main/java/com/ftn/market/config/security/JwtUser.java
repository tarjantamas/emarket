package com.ftn.market.config.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class JwtUser extends User {

  private final Long id;

  public JwtUser(final String username, final String password, final boolean enabled, final boolean accountNonExpired,
      final boolean credentialsNonExpired, final boolean accountNonLocked,
      final Collection<? extends GrantedAuthority> authorities, final Long id) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
