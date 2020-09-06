package com.ftn.market.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.ftn.market.dto.product.ProductResponse;
import com.ftn.market.dto.product.RegisterProduct;
import com.ftn.market.entity.Product;

public class ProductTestUtils extends TestUtils {

  public static RegisterProduct buildValidDtoForRegistration() {
    return RegisterProduct.builder()
      .name("Krastavac")
      .description("Domaci krastavac")
      .unit("kg")
      .price(99.00)
      .companyId(1L)
      .build();
  }

  public static void verifyReturnedProductResponse(final ProductResponse actual, final Product expected) {
    assertThat(actual.getId(), is(expected.getId()));
    assertThat(actual.getName(), is(expected.getName()));
    assertThat(actual.getDescription(), is(expected.getDescription()));
    assertThat(actual.getPrice(), is(expected.getPrice()));
    assertThat(actual.getCompanyId(), is(expected.getCompany().getId()));
  }
}
