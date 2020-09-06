package com.ftn.market.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import com.ftn.market.dto.user.RegisterUser;
import com.ftn.market.dto.user.UpdateUser;
import com.ftn.market.dto.user.UserResponse;
import com.ftn.market.entity.User;

public class UserTestUtils extends TestUtils {

  public static RegisterUser buildValidForRegistration() {
    return RegisterUser.builder()
      .email("registered@market.com")
      .password("password")
      .firstName("Registered")
      .lastName("Account")
      .build();
  }

  public static UpdateUser buildValidForUpdate() {
    return UpdateUser.builder().firstName("New first name").lastName("new last name").build();
  }

  public static void verifyReturnedUserResponse(final UserResponse actual, final User expected) {
    assertThat(actual.getId(), is(expected.getId()));
    assertThat(actual.getEmail(), is(expected.getEmail()));
    assertThat(actual.getFirstName(), is(expected.getFirstName()));
    assertThat(actual.getLastName(), is(expected.getLastName()));
    assertNotNull(expected.getPassword());
  }
}
