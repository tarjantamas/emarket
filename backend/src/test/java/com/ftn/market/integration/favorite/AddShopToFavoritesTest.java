package com.ftn.market.integration.favorite;

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
import com.ftn.market.entity.Favorite;

public class AddShopToFavoritesTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/favorites/shop";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnNotFoundWhenShopDoesNotExist() {
    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/9999", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(), ApiError.class);

    verifyNotFoundResponse(apiErrorResponse, ErrorCodes.SHOP_NOT_FOUND_3);
  }

  @Test
  public void shouldReturnBadRequestWhenShopIsAlreadyFavorite() {
    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/1", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.FAVORITE_SHOP_ALREADY_EXISTS);
  }

  @Test
  public void shouldReturnNoContentAfterSuccessfulFavorite() {
    final ResponseEntity<Void> response = testRestTemplate.exchange(URL + "/2", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(), Void.class);

    assertThat(response.getStatusCode(), Matchers.is(HttpStatus.NO_CONTENT));
    final Favorite favorite =
        entityManager.createQuery("SELECT f FROM Favorite f WHERE f.user.id = 2 AND f.shop.id = 2", Favorite.class)
          .getSingleResult();
    assertNotNull(favorite);
  }
}
