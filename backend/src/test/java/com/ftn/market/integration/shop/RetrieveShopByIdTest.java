package com.ftn.market.integration.shop;

import static com.ftn.market.util.ShopTestUtils.verifyReturnedShopResponse;
import static com.ftn.market.util.TestUtils.verifyNotFoundResponse;
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
import com.ftn.market.dto.shop.ShopResponse;
import com.ftn.market.entity.Shop;

public class RetrieveShopByIdTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/shop";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnNotFoundWhenRetrievingNonExistingShop() {
    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/99999", HttpMethod.GET,
        testHelper.createRequestEntityRegisteredUser(), ApiError.class);

    verifyNotFoundResponse(apiErrorResponse, ErrorCodes.SHOP_NOT_FOUND_1);
  }

  @Test
  public void shouldReturnOkAndExistingShop() {
    final ResponseEntity<ShopResponse> shopResponse = testRestTemplate.exchange(URL + "/1", HttpMethod.GET,
        testHelper.createRequestEntityRegisteredUser(), ShopResponse.class);

    assertThat(shopResponse.getStatusCode(), is(HttpStatus.OK));
    final ShopResponse actual = shopResponse.getBody();
    final Shop expected = entityManager.find(Shop.class, 1L);
    assertNotNull(actual);
    assertNotNull(expected);
    verifyReturnedShopResponse(actual, expected);
  }
}
