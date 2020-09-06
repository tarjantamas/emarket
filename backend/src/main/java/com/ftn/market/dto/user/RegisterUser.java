package com.ftn.market.dto.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.ftn.market.constants.RegexPatterns;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUser {

  @NotNull
  @Pattern(regexp = RegexPatterns.EMAIL, message = "Invalid email address")
  private String email;

  @NotNull
  private String password;

  @NotNull
  private String firstName;

  @NotNull
  private String lastName;
}
