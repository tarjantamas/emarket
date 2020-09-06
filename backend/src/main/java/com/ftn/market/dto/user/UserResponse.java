package com.ftn.market.dto.user;

import java.util.List;

import lombok.Data;

@Data
public class UserResponse {

  private Long id;

  private String email;

  private String firstName;

  private String lastName;

  private List<Long> companyIds;
}
