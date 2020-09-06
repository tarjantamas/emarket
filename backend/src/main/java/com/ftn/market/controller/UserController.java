package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.market.dto.user.UpdateUser;
import com.ftn.market.dto.user.UserResponse;
import com.ftn.market.entity.User;
import com.ftn.market.mapper.UserMapper;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

  private final UserMapper userMapper;
  private final UserService userService;

  @GetMapping(path = "/me", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> findMyInfo() {
    return ResponseEntity.ok(userMapper.mapToFullResponse(userService.findMyInfo()));
  }

  @PutMapping(path = "/me", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> updateMyInfo(@RequestBody final UpdateUser updateUser) {
    final User user = userService.updateMyInfo(updateUser);

    return ResponseEntity.ok(userMapper.mapToFullResponse(user));
  }
}
