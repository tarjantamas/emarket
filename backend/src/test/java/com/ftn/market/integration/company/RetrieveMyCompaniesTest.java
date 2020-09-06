package com.ftn.market.integration.company;

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
import com.ftn.market.dto.company.CompanyResponse;

public class RetrieveMyCompaniesTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/companies/my";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  public void shouldReturnOkAndMyCompanies_1() {
    final ResponseEntity<CompanyResponse[]> companiesResponse = testRestTemplate.exchange(URL, HttpMethod.GET,
        testHelper.createRequestEntityRegisteredUser(), CompanyResponse[].class);

    assertThat(companiesResponse.getStatusCode(), is(HttpStatus.OK));
    final CompanyResponse[] actual = companiesResponse.getBody();
    assertNotNull(actual);
    assertThat(actual.length, Matchers.is(1));
  }

  @Test
  public void shouldReturnOkAndMyCompanies_2() {
    final ResponseEntity<CompanyResponse[]> companiesResponse = testRestTemplate.exchange(URL, HttpMethod.GET,
        testHelper.createRequestEntityAdminUser(), CompanyResponse[].class);

    assertThat(companiesResponse.getStatusCode(), is(HttpStatus.OK));
    final CompanyResponse[] actual = companiesResponse.getBody();
    assertNotNull(actual);
    assertThat(actual.length, Matchers.is(0));
  }
}
