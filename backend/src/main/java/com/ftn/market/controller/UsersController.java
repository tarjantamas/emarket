package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.market.dto.user.RegisterUser;
import com.ftn.market.dto.user.UserResponse;
import com.ftn.market.entity.User;
import com.ftn.market.mapper.UserMapper;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UsersController {

  private final UserMapper userMapper;
  private final UserService userService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> register(@RequestBody @Validated final RegisterUser registerUser) {
    final User user = userService.register(registerUser);

    return new ResponseEntity<>(userMapper.mapToFullResponse(user), HttpStatus.CREATED);
  }
}
