package com.ftn.market.integration.company;

import static com.ftn.market.util.CompanyTestUtils.verifyReturnedCompanyResponse;
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
import com.ftn.market.dto.company.CompanyResponse;
import com.ftn.market.entity.Company;

public class RetrieveCompanyByIdTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/company";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnNotFoundWhenRetrievingNonExistingCompany() {
    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/99999", HttpMethod.GET,
        testHelper.createRequestEntityRegisteredUser(), ApiError.class);

    verifyNotFoundResponse(apiErrorResponse, ErrorCodes.COMPANY_NOT_FOUND_2);
  }

  @Test
  public void shouldReturnOkAndExistingCompany() {
    final ResponseEntity<CompanyResponse> companyResponse = testRestTemplate.exchange(URL + "/1", HttpMethod.GET,
        testHelper.createRequestEntityRegisteredUser(), CompanyResponse.class);

    assertThat(companyResponse.getStatusCode(), is(HttpStatus.OK));
    final CompanyResponse actual = companyResponse.getBody();
    final Company expected = entityManager.find(Company.class, 1L);
    assertNotNull(actual);
    assertNotNull(expected);
    verifyReturnedCompanyResponse(actual, expected);
  }
}
