package com.ftn.market.integration.subscription;

import static com.ftn.market.util.TestUtils.verifyBadRequestResponse;
import static com.ftn.market.util.TestUtils.verifyNotFoundResponse;
import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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

public class UnsubscribeFromCompanyTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/subscription/company";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnNotFoundWhenCompanyDoesNotExist() {
    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/9999", HttpMethod.DELETE,
        testHelper.createRequestEntityRegisteredUser(), ApiError.class);

    verifyNotFoundResponse(apiErrorResponse, ErrorCodes.COMPANY_NOT_FOUND_6);
  }

  @Test
  public void shouldReturnBadRequestWhenYouAreNotSubscribedToCompany() {
    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/1", HttpMethod.DELETE,
        testHelper.createRequestEntityAdminUser(), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.COMPANY_NOT_SUBSCRIBED);
  }

  @Test
  public void shouldReturnNoContentAndRemoveCompanySubscription() {
    final ResponseEntity<Void> response = testRestTemplate.exchange(URL + "/1", HttpMethod.DELETE,
        testHelper.createRequestEntityRegisteredUser(), Void.class);

    assertThat(response.getStatusCode(), Matchers.is(HttpStatus.NO_CONTENT));
    try {
      entityManager
        .createQuery("SELECT s FROM Subscription s WHERE s.user.id = 2 AND s.company.id = 1", Subscription.class)
        .getSingleResult();
      fail("Unsuccessful unsubscription from company.");
    } catch (final NoResultException exception) {
      // success
    }
  }
}