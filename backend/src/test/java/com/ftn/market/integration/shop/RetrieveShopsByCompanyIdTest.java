package com.ftn.market.integration.shop;

import static com.ftn.market.util.TestUtils.verifyNotFoundResponse;
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
import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.dto.ApiError;
import com.ftn.market.dto.shop.ShopResponse;

public class RetrieveShopsByCompanyIdTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/shops/company";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  public void shouldReturnNotFoundWhenCompanyDoesNotExist() {
    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/9999", HttpMethod.GET,
        testHelper.createRequestEntityRegisteredUser(), ApiError.class);

    verifyNotFoundResponse(apiErrorResponse, ErrorCodes.COMPANY_NOT_FOUND_4);
  }

  @Test
  public void shouldReturnOkAndShopsForCompany_1() {
    final ResponseEntity<ShopResponse[]> shopsResponse = testRestTemplate.exchange(URL + "/1", HttpMethod.GET,
        testHelper.createRequestEntityRegisteredUser(), ShopResponse[].class);

    assertThat(shopsResponse.getStatusCode(), is(HttpStatus.OK));
    final ShopResponse[] actual = shopsResponse.getBody();
    assertNotNull(actual);
    assertThat(actual.length, Matchers.is(2));
  }

  @Test
  public void shouldReturnOkAndShopsForCompany_2() {
    final ResponseEntity<ShopResponse[]> shopsResponse = testRestTemplate.exchange(URL + "/2", HttpMethod.GET,
        testHelper.createRequestEntityRegisteredUser(), ShopResponse[].class);

    assertThat(shopsResponse.getStatusCode(), is(HttpStatus.OK));
    final ShopResponse[] actual = shopsResponse.getBody();
    assertNotNull(actual);
    assertThat(actual.length, Matchers.is(0));
  }
}
