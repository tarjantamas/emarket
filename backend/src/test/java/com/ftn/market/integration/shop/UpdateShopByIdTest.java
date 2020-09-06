package com.ftn.market.integration.shop;

import static com.ftn.market.util.ShopTestUtils.buildValidDtoForUpdate;
import static com.ftn.market.util.ShopTestUtils.verifyReturnedShopResponse;
import static com.ftn.market.util.TestUtils.verifyBadRequestResponse;
import static com.ftn.market.util.TestUtils.verifyNotFoundResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

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
import com.ftn.market.dto.shop.UpdateShop;
import com.ftn.market.entity.Product;
import com.ftn.market.entity.Shop;

public class UpdateShopByIdTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/shop";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnNotFoundWhenShopDoesNotExist() {
    final UpdateShop updateShop = buildValidDtoForUpdate();

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/9999", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(updateShop), ApiError.class);

    verifyNotFoundResponse(apiErrorResponse, ErrorCodes.SHOP_NOT_FOUND_5);
  }

  @Test
  public void shouldReturnBadRequestWhenRequesterIsNotShopsCompanyOwner() {
    final UpdateShop updateShop = buildValidDtoForUpdate();

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/1", HttpMethod.PUT,
        testHelper.createRequestEntityAdminUser(updateShop), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.SHOP_UPDATE_REQUESTER_NOT_COMPANY_OWNER);
  }

  @Test
  public void shouldReturnNotFoundWhenOneOrMoreProductsDoesNotExist() {
    final UpdateShop updateShop = buildValidDtoForUpdate();
    updateShop.setProductIds(Arrays.asList(1L, 9999L));

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/1", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(updateShop), ApiError.class);

    verifyNotFoundResponse(apiErrorResponse, ErrorCodes.PRODUCT_NOT_FOUND_2);
  }

  @Test
  public void shouldReturnOkAndShop_1() {
    final UpdateShop updateShop = buildValidDtoForUpdate();

    final ResponseEntity<ShopResponse> shopResponse = testRestTemplate.exchange(URL + "/1", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(updateShop), ShopResponse.class);

    assertThat(shopResponse.getStatusCode(), is(HttpStatus.OK));
    final ShopResponse actual = shopResponse.getBody();
    final Shop expected = entityManager.find(Shop.class, actual.getId());
    assertNotNull(actual);
    assertNotNull(expected);
    verifyReturnedShopResponse(actual, expected);

    assertThat(getProductsForShop(1L), is(1));
    assertThat(getProductsForShop(2L), is(0));
  }

  @Test
  public void shouldReturnOkAndShop_2() {
    final UpdateShop updateShop = buildValidDtoForUpdate();

    final ResponseEntity<ShopResponse> shopResponse = testRestTemplate.exchange(URL + "/2", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(updateShop), ShopResponse.class);

    assertThat(shopResponse.getStatusCode(), is(HttpStatus.OK));
    final ShopResponse actual = shopResponse.getBody();
    final Shop expected = entityManager.find(Shop.class, actual.getId());
    assertNotNull(actual);
    assertNotNull(expected);
    verifyReturnedShopResponse(actual, expected);

    assertThat(getProductsForShop(1L), is(2));
    assertThat(getProductsForShop(2L), is(1));
  }

  private int getProductsForShop(final Long shopId) {
    final String sql = String.format(
        "SELECT * FROM products p JOIN products_shops ps ON p.id = ps.fk_product_id WHERE ps.fk_shop_id = %d;", shopId);
    return entityManager.createNativeQuery(sql, Product.class).getResultList().size();
  }
}
