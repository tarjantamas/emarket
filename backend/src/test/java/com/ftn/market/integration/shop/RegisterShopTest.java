package com.ftn.market.integration.shop;

import static com.ftn.market.util.ShopTestUtils.buildValidDtoForRegistration;
import static com.ftn.market.util.ShopTestUtils.verifyReturnedShopResponse;
import static com.ftn.market.util.TestUtils.verifyBadRequestResponse;
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
import com.ftn.market.dto.shop.RegisterShop;
import com.ftn.market.dto.shop.ShopResponse;
import com.ftn.market.entity.Shop;

public class RegisterShopTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/shops";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnBadRequestWhenNameIsNull() {
    final RegisterShop registerShop = buildValidDtoForRegistration();
    registerShop.setName(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerShop), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenDescriptionIsNull() {
    final RegisterShop registerShop = buildValidDtoForRegistration();
    registerShop.setDescription(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerShop), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenLatitudeIsNull() {
    final RegisterShop registerShop = buildValidDtoForRegistration();
    registerShop.setLatitude(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerShop), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenLongitudeIsNull() {
    final RegisterShop registerShop = buildValidDtoForRegistration();
    registerShop.setLongitude(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerShop), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenCompanyIdIsNull() {
    final RegisterShop registerShop = buildValidDtoForRegistration();
    registerShop.setCompanyId(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerShop), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnNotFoundWhenCompanyDoesNotExist() {
    final RegisterShop registerShop = buildValidDtoForRegistration();
    registerShop.setCompanyId(9999L);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerShop), ApiError.class);

    verifyNotFoundResponse(apiErrorResponse, ErrorCodes.COMPANY_NOT_FOUND_1);
  }

  @Test
  public void shouldReturnBadRequestWhenRequesterIsNotCompanyOwner() {
    final RegisterShop registerShop = buildValidDtoForRegistration();

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityAdminUser(registerShop), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.SHOP_CREATE_REQUESTER_NOT_COMPANY_OWNER);
  }

  @Test
  public void shouldReturnCreatedAndShop() {
    final RegisterShop registerShop = buildValidDtoForRegistration();

    final ResponseEntity<ShopResponse> shopResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerShop), ShopResponse.class);

    assertThat(shopResponse.getStatusCode(), is(HttpStatus.CREATED));
    final ShopResponse actual = shopResponse.getBody();
    final Shop expected = entityManager.find(Shop.class, actual.getId());
    assertNotNull(actual);
    assertNotNull(expected);
    verifyReturnedShopResponse(actual, expected);
  }
}
