package com.ftn.market.entity;

public enum Roles {
  ROLE_ADMIN(1L, "ROLE_ADMIN"),
  ROLE_REGISTERED_USER(2L, "ROLE_REGISTERED_USER");

  private final Role role;

  Roles(final Long id, final String name) {
    final Role role = new Role();
    role.setId(id);
    role.setName(name);
    this.role = role;
  }

  public Role getRole() {
    return this.role;
  }

  public Long getId() {
    return this.role.getId();
  }
}
