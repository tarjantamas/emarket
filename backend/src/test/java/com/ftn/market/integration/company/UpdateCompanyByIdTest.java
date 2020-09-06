package com.ftn.market.integration.company;

import static com.ftn.market.util.CompanyTestUtils.buildValidDtoForUpdate;
import static com.ftn.market.util.CompanyTestUtils.verifyReturnedCompanyResponse;
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
import com.ftn.market.dto.company.CompanyResponse;
import com.ftn.market.dto.company.UpdateCompany;
import com.ftn.market.entity.Company;

public class UpdateCompanyByIdTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/company";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnNotFoundWhenCompanyDoesNotExist() {
    final UpdateCompany updateCompany = buildValidDtoForUpdate();

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/9999", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(updateCompany), ApiError.class);

    verifyNotFoundResponse(apiErrorResponse, ErrorCodes.COMPANY_NOT_FOUND_8);
  }

  @Test
  public void shouldReturnBadRequestWhenRequesterIsNotCompanyOwner() {
    final UpdateCompany updateCompany = buildValidDtoForUpdate();

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL + "/1", HttpMethod.PUT,
        testHelper.createRequestEntityAdminUser(updateCompany), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.COMPANY_UPDATE_REQUESTER_NOT_OWNER);
  }

  @Test
  public void shouldReturnOkAndCompany() {
    final UpdateCompany updateCompany = buildValidDtoForUpdate();

    final ResponseEntity<CompanyResponse> companyResponse = testRestTemplate.exchange(URL + "/1", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(updateCompany), CompanyResponse.class);

    assertThat(companyResponse.getStatusCode(), is(HttpStatus.OK));
    final CompanyResponse actual = companyResponse.getBody();
    final Company expected = entityManager.find(Company.class, actual.getId());
    assertNotNull(actual);
    assertNotNull(expected);
    verifyReturnedCompanyResponse(actual, expected);
  }
}
