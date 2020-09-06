package com.ftn.market.integration.favorite;

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
import com.ftn.market.dto.favorite.FavoriteResponse;

public class RetrieveFavoriteShopsTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/favorites/shops";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  public void shouldReturnOkAndFavoriteShops_1() {
    final ResponseEntity<FavoriteResponse[]> favoritesResponse = testRestTemplate.exchange(URL, HttpMethod.GET,
        testHelper.createRequestEntityRegisteredUser(), FavoriteResponse[].class);

    assertThat(favoritesResponse.getStatusCode(), is(HttpStatus.OK));
    final FavoriteResponse[] actual = favoritesResponse.getBody();
    assertNotNull(actual);
    assertThat(actual.length, Matchers.is(1));
  }

  @Test
  public void shouldReturnOkAndFavoriteShops_2() {
    final ResponseEntity<FavoriteResponse[]> favoritesResponse = testRestTemplate.exchange(URL, HttpMethod.GET,
        testHelper.createRequestEntityAdminUser(), FavoriteResponse[].class);

    assertThat(favoritesResponse.getStatusCode(), is(HttpStatus.OK));
    final FavoriteResponse[] actual = favoritesResponse.getBody();
    assertNotNull(actual);
    assertThat(actual.length, Matchers.is(0));
  }
}
