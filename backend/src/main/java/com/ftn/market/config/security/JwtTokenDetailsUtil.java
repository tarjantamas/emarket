package com.ftn.market.config.security;

import java.util.ArrayList;

import org.springframework.security.core.context.SecurityContextHolder;

public class JwtTokenDetailsUtil {

  public static Long getCurrentUserId() {
    return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
  }

  public static String getCurrentUserEmail() {
    return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
  }

  public static String getCurrentUserRole() {
    return new ArrayList<>(
        ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities()).get(0)
          .getAuthority();
  }
}
