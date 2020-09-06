package com.ftn.market.integration.shop;

import static com.ftn.market.util.ProductTestUtils.verifyReturnedProductResponse;
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
import com.ftn.market.entity.Product;

public class RetrieveProductByIdTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/product";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnNotFoundWhenRetrievingNonExistingProduct() {
    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/99999", HttpMethod.GET,
        testHelper.createRequestEntityRegisteredUser(), ApiError.class);

    verifyNotFoundResponse(apiErrorResponse, ErrorCodes.PRODUCT_NOT_FOUND_1);
  }

  @Test
  public void shouldReturnOkAndExistingShop() {
    final ResponseEntity<ProductResponse> productResponse = testRestTemplate.exchange(URL + "/1", HttpMethod.GET,
        testHelper.createRequestEntityRegisteredUser(), ProductResponse.class);

    assertThat(productResponse.getStatusCode(), is(HttpStatus.OK));
    final ProductResponse actual = productResponse.getBody();
    final Product expected = entityManager.find(Product.class, 1L);
    assertNotNull(actual);
    assertNotNull(expected);
    verifyReturnedProductResponse(actual, expected);
  }
}
