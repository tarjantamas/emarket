package com.ftn.market.config.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ftn.market.entity.User;
import com.ftn.market.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public JwtUser loadUserByUsername(final String email) throws UsernameNotFoundException {
    final Optional<User> optionalUser = userRepository.findByEmail(email);

    if (!optionalUser.isPresent()) {
      throw new UsernameNotFoundException("User not found with email: " + email);
    } else {
      final User user = optionalUser.get();
      final List<GrantedAuthority> grantedAuthorities =
          user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

      return new JwtUser(email, user.getPassword(), true, true, true, true, grantedAuthorities, user.getId());
    }
  }
}
