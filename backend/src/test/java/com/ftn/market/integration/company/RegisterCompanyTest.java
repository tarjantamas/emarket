package com.ftn.market.integration.company;

import static com.ftn.market.util.CompanyTestUtils.buildValidDtoForRegistration;
import static com.ftn.market.util.CompanyTestUtils.verifyReturnedCompanyResponse;
import static com.ftn.market.util.TestUtils.verifyBadRequestResponse;
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
import com.ftn.market.dto.company.RegisterCompany;
import com.ftn.market.entity.Company;

public class RegisterCompanyTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/companies";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnBadRequestWhenNameIsNull() {
    final RegisterCompany registerCompany = buildValidDtoForRegistration();
    registerCompany.setName(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerCompany), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenDescriptionIsNull() {
    final RegisterCompany registerCompany = buildValidDtoForRegistration();
    registerCompany.setDescription(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerCompany), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenVatIsNull() {
    final RegisterCompany registerCompany = buildValidDtoForRegistration();
    registerCompany.setVat(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerCompany), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenRidIsNull() {
    final RegisterCompany registerCompany = buildValidDtoForRegistration();
    registerCompany.setRid(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerCompany), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenCountryIsNull() {
    final RegisterCompany registerCompany = buildValidDtoForRegistration();
    registerCompany.setCountry(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerCompany), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenCityIsNull() {
    final RegisterCompany registerCompany = buildValidDtoForRegistration();
    registerCompany.setCity(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerCompany), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnBadRequestWhenAddressIsNull() {
    final RegisterCompany registerCompany = buildValidDtoForRegistration();
    registerCompany.setAddress(null);

    final ResponseEntity<ApiError> apiErrorResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerCompany), ApiError.class);

    verifyBadRequestResponse(apiErrorResponse, ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @Test
  public void shouldReturnCreatedAndCompany() {
    final RegisterCompany registerCompany = buildValidDtoForRegistration();

    final ResponseEntity<CompanyResponse> companyResponse = testRestTemplate.exchange(URL, HttpMethod.POST,
        testHelper.createRequestEntityRegisteredUser(registerCompany), CompanyResponse.class);

    assertThat(companyResponse.getStatusCode(), is(HttpStatus.CREATED));
    final CompanyResponse actual = companyResponse.getBody();
    final Company expected = entityManager.find(Company.class, actual.getId());
    assertNotNull(actual);
    assertNotNull(expected);
    verifyReturnedCompanyResponse(actual, expected);
  }
}
