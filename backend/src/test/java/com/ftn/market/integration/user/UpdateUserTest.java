package com.ftn.market.integration.user;

import static com.ftn.market.util.UserTestUtils.buildValidForUpdate;
import static com.ftn.market.util.UserTestUtils.verifyReturnedUserResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ftn.market.BaseIntegrationTestSetup;
import com.ftn.market.TestHelper;
import com.ftn.market.dto.user.UpdateUser;
import com.ftn.market.dto.user.UserResponse;
import com.ftn.market.entity.User;

public class UpdateUserTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/user";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnOkAndUpdatedUser() {
    final UpdateUser updateUser = buildValidForUpdate();

    final ResponseEntity<UserResponse> userResponse = testRestTemplate.exchange(URL + "/me", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(updateUser), UserResponse.class);

    assertThat(userResponse.getStatusCode(), is(HttpStatus.OK));
    final UserResponse actual = userResponse.getBody();
    assertNotNull(actual);
    final User expected =
        entityManager.createQuery("SELECT u FROM User u WHERE u.email = 'user1@market.com'", User.class)
          .getSingleResult();
    assertNotNull(actual);
    assertNotNull(expected);
    verifyReturnedUserResponse(actual, expected);
  }
}
