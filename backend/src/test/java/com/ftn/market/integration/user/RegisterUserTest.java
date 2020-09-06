package com.ftn.market.integration.user;

import static com.ftn.market.util.TestUtils.verifyBadRequestResponse;
import static com.ftn.market.util.UserTestUtils.buildValidForRegistration;
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
import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.dto.ApiError;
import com.ftn.market.dto.user.RegisterUser;
import com.ftn.market.dto.user.UserResponse;
import com.ftn.market.entity.Setting;
import com.ftn.market.entity.User;

public class RegisterUserTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/users";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnBadRequestWhenEmailIsNull() {
    final RegisterUser registerUser = buildValidForRegistration();
    registerUser.setEmail(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityWithoutAuthorization(registerUser), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenEmailIsNotValid() {
    final RegisterUser registerUser = buildValidForRegistration();
    registerUser.setEmail("invalid_email.format");

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityWithoutAuthorization(registerUser), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenFirstNameIsNull() {
    final RegisterUser registerUser = buildValidForRegistration();
    registerUser.setFirstName(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityWithoutAuthorization(registerUser), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenLastNameIsNull() {
    final RegisterUser registerUser = buildValidForRegistration();
    registerUser.setLastName(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityWithoutAuthorization(registerUser), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenPasswordIsNull() {
    final RegisterUser registerUser = buildValidForRegistration();
    registerUser.setPassword(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityWithoutAuthorization(registerUser), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenUserAlreadyExistsWithEmail() {
    final RegisterUser registerUser = buildValidForRegistration();
    registerUser.setEmail("admin1@market.com");

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityWithoutAuthorization(registerUser), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.USER_ALREADY_EXISTS_BY_EMAIL);
  }

  @Test
  public void shouldReturnCreatedAndUser() {
    final RegisterUser registerUser = buildValidForRegistration();

    final ResponseEntity<UserResponse> userResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityWithoutAuthorization(registerUser), UserResponse.class);

    assertThat(userResponse.getStatusCode(), is(HttpStatus.CREATED));
    final UserResponse actual = userResponse.getBody();
    final User expected = entityManager
      .createQuery(String.format("SELECT u FROM User u WHERE u.email = '%s'", registerUser.getEmail()), User.class)
      .getSingleResult();
    assertNotNull(actual);
    verifyReturnedUserResponse(actual, expected);
    final Setting setting = entityManager
      .createQuery(String.format("SELECT s FROM Setting s WHERE s.user.email = '%s'", registerUser.getEmail()),
          Setting.class)
      .getSingleResult();
    assertNotNull(setting);
  }
}
