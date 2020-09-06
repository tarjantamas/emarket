package com.ftn.market.integration.product;

import static com.ftn.market.util.ProductTestUtils.buildValidDtoForRegistration;
import static com.ftn.market.util.ProductTestUtils.verifyReturnedProductResponse;
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
import com.ftn.market.dto.product.ProductResponse;
import com.ftn.market.dto.product.RegisterProduct;
import com.ftn.market.entity.Product;

public class RegisterProductTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/products";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnBadRequestWhenNameIsNull() {
    final RegisterProduct registerProduct = buildValidDtoForRegistration();
    registerProduct.setName(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerProduct), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenDescriptionIsNull() {
    final RegisterProduct registerProduct = buildValidDtoForRegistration();
    registerProduct.setDescription(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerProduct), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenPriceIsNull() {
    final RegisterProduct registerProduct = buildValidDtoForRegistration();
    registerProduct.setPrice(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerProduct), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenCompanyIdIsNull() {
    final RegisterProduct registerProduct = buildValidDtoForRegistration();
    registerProduct.setCompanyId(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerProduct), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnNotFoundWhenCompanyDoesNotExist() {
    final RegisterProduct registerProduct = buildValidDtoForRegistration();
    registerProduct.setCompanyId(9999L);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerProduct), ApiError.class);

    verifyNotFoundResponse(apiErrorResponse, ErrorCodes.COMPANY_NOT_FOUND_3);
  }

  @Test
  public void shouldReturnBadRequestWhenRequesterIsNotCompanyOwner() {
    final RegisterProduct registerProduct = buildValidDtoForRegistration();

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityAdminUser(registerProduct), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.PRODUCT_CREATE_REQUESTER_NOT_COMPANY_OWNER);
  }

  @Test
  public void shouldReturnCreatedAndProduct() {
    final RegisterProduct registerProduct = buildValidDtoForRegistration();

    final ResponseEntity<ProductResponse> productResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerProduct), ProductResponse.class);

    assertThat(productResponse.getStatusCode(), is(HttpStatus.CREATED));
    final ProductResponse actual = productResponse.getBody();
    final Product expected = entityManager.find(Product.class, actual.getId());
    assertNotNull(actual);
    assertNotNull(expected);
    verifyReturnedProductResponse(actual, expected);
  }
}
