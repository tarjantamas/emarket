package com.ftn.market.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.ftn.market.dto.company.CompanyResponse;
import com.ftn.market.dto.company.RegisterCompany;
import com.ftn.market.dto.company.UpdateCompany;
import com.ftn.market.entity.Company;

public class CompanyTestUtils extends TestUtils {

  public static RegisterCompany buildValidDtoForRegistration() {
    return RegisterCompany.builder()
      .name("Neko random ime")
      .description("Poljoprivredno gazdinstvo")
      .vat(5678)
      .rid(1234)
      .country("Srbija")
      .city("Novi Sad")
      .address("Bulevar Oslobodjenja 1")
      .build();
  }

  public static UpdateCompany buildValidDtoForUpdate() {
    return UpdateCompany.builder()
      .name("Company name updated")
      .description("Company description updated")
      .vat(5555)
      .rid(8888)
      .country("Company country updated")
      .city("Company city updated")
      .build();
  }

  public static void verifyReturnedCompanyResponse(final CompanyResponse actual, final Company expected) {
    assertThat(actual.getId(), is(expected.getId()));
    assertThat(actual.getAddress(), is(expected.getAddress()));
    assertThat(actual.getCity(), is(expected.getCity()));
    assertThat(actual.getDescription(), is(expected.getDescription()));
    assertThat(actual.getName(), is(expected.getName()));
    assertThat(actual.getRid(), is(expected.getRid()));
    assertThat(actual.getVat(), is(expected.getVat()));
    assertThat(actual.getUserId(), is(expected.getUser().getId()));
  }
}
