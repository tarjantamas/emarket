package com.ftn.market.integration.subscription;

import static com.ftn.market.util.TestUtils.verifyBadRequestResponse;
import static com.ftn.market.util.TestUtils.verifyNotFoundResponse;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
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
import com.ftn.market.entity.Subscription;

public class SubscribeToCompanyTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/subscriptions/company";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnNotFoundWhenCompanyDoesNotExist() {
    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/9999", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(), ApiError.class);

    verifyNotFoundResponse(apiErrorResponse, ErrorCodes.COMPANY_NOT_FOUND_5);
  }

  @Test
  public void shouldReturnBadRequestWhenYouAreAlreadySubscribedToCompany() {
    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/1", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.SUBSCRIPTION_COMPANY_ALREADY_EXISTS);
  }

  @Test
  public void shouldReturnNoContentAfterSuccessfulSubscription() {
    final ResponseEntity<Void> response = testRestTemplate.exchange(URL + "/2", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(), Void.class);

    assertThat(response.getStatusCode(), Matchers.is(HttpStatus.NO_CONTENT));
    final Subscription subscription = entityManager
      .createQuery("SELECT s FROM Subscription s WHERE s.user.id = 2 AND s.company.id = 2", Subscription.class)
      .getSingleResult();
    assertNotNull(subscription);
  }
}
