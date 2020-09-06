package com.ftn.market.integration.subscription;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ftn.market.BaseIntegrationTestSetup;
import com.ftn.market.TestHelper;
import com.ftn.market.dto.subscription.SubscriptionResponse;

public class RetrieveCompanySubscriptionsTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/subscriptions/companies";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  public void shouldReturnOkAndCompanySubscriptions_1() {
    final ResponseEntity<SubscriptionResponse[]> subscriptionsResponse = testRestTemplate.exchange(URL, HttpMethod.GET,
        testHelper.createRequestEntityRegisteredUser(), SubscriptionResponse[].class);

    assertThat(subscriptionsResponse.getStatusCode(), is(HttpStatus.OK));
    final SubscriptionResponse[] actual = subscriptionsResponse.getBody();
    assertNotNull(actual);
    assertThat(actual.length, Matchers.is(1));
  }

  @Test
  public void shouldReturnOkAndCompanySubscriptions_2() {
    final ResponseEntity<SubscriptionResponse[]> subscriptionsResponse = testRestTemplate.exchange(URL, HttpMethod.GET,
        testHelper.createRequestEntityAdminUser(), SubscriptionResponse[].class);

    assertThat(subscriptionsResponse.getStatusCode(), is(HttpStatus.OK));
    final SubscriptionResponse[] actual = subscriptionsResponse.getBody();
    assertNotNull(actual);
    assertThat(actual.length, Matchers.is(0));
  }
}
