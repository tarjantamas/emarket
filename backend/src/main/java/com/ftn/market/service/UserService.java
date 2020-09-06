package com.ftn.market.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.market.config.security.JwtUser;
import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.dto.user.RegisterUser;
import com.ftn.market.dto.user.UpdateUser;
import com.ftn.market.entity.Roles;
import com.ftn.market.entity.User;
import com.ftn.market.exception.BadRequestException;
import com.ftn.market.exception.InternalServerException;
import com.ftn.market.exception.NotLoggedInException;
import com.ftn.market.mapper.UserMapper;
import com.ftn.market.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final SettingService settingService;
  private final UserMapper userMapper;
  private final UserRepository userRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public User register(final RegisterUser registerUser) {
    if (userRepository.findByEmail(registerUser.getEmail()).isPresent()) {
      throw new BadRequestException(ErrorCodes.USER_ALREADY_EXISTS_BY_EMAIL);
    }

    User user = userMapper.mapToDBO(registerUser);
    user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
    user.setRoles(Collections.singleton(Roles.ROLE_REGISTERED_USER.getRole()));

    user = userRepository.save(user);
    settingService.create(user);

    log.info("Successfully created {}", user);
    return user;
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public User findMyInfo() {
    return getActiveUserOrFail();
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public User getActiveUserOrFail() {
    final String email = getActiveUserEmail();
    final Optional<User> oUser = userRepository.findByEmail(email);

    if (!oUser.isPresent()) {
      log.info("User with email '{}' not found", email);
      throw new InternalServerException(ErrorCodes.USER_NOT_FOUND_1);
    }
    return oUser.get();
  }

  private String getActiveUserEmail() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
      throw new NotLoggedInException();
    }

    final UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
    if (auth.getPrincipal() == null) {
      throw new NotLoggedInException();
    }

    return ((JwtUser) auth.getPrincipal()).getUsername();
  }

  @Transactional
  public User updateMyInfo(final UpdateUser updateUser) {
    User user = getActiveUserOrFail();

    userMapper.mapToDBO(updateUser, user);

    if (updateUser.getPassword() != null) {
      user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
    }

    user = userRepository.save(user);
    log.info("successfully updated {}", user);

    return user;
  }
}
