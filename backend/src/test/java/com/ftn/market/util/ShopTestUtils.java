package com.ftn.market.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collections;

import com.ftn.market.dto.shop.RegisterShop;
import com.ftn.market.dto.shop.ShopResponse;
import com.ftn.market.dto.shop.UpdateShop;
import com.ftn.market.entity.Shop;

public class ShopTestUtils extends TestUtils {

  public static RegisterShop buildValidDtoForRegistration() {
    return RegisterShop.builder()
      .name("Rodic")
      .description("Veleprodaja na Klisi")
      .latitude(45.305121)
      .longitude(19.831552)
      .companyId(1L)
      .build();
  }

  public static UpdateShop buildValidDtoForUpdate() {
    return UpdateShop.builder()
      .name("Shop name updated")
      .description("Shop description updated")
      .longitude(11.00)
      .latitude(22.00)
      .productIds(Collections.singletonList(1L))
      .build();
  }

  public static void verifyReturnedShopResponse(final ShopResponse actual, final Shop expected) {
    assertThat(actual.getId(), is(expected.getId()));
    assertThat(actual.getName(), is(expected.getName()));
    assertThat(actual.getDescription(), is(expected.getDescription()));
    assertThat(actual.getLatitude(), is(expected.getLatitude()));
    assertThat(actual.getLongitude(), is(expected.getLongitude()));
    assertThat(actual.getCompanyId(), is(expected.getCompany().getId()));
  }
}
