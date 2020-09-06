package com.ftn.market.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ftn.market.dto.ApiError;

public class TestUtils {

  public static void verifyBadRequestResponse(final ResponseEntity<ApiError> testApiErrorResponse,
      final String errorCode) {
    verifyErrorResponse(testApiErrorResponse, HttpStatus.BAD_REQUEST, errorCode);
  }

  public static void verifyNotFoundResponse(final ResponseEntity<ApiError> testApiErrorResponse,
      final String errorCode) {
    verifyErrorResponse(testApiErrorResponse, HttpStatus.NOT_FOUND, errorCode);
  }

  public static void verifyErrorResponse(final ResponseEntity<ApiError> testApiErrorResponse,
      final HttpStatus httpStatus, final String errorCode) {
    final ApiError apiError = testApiErrorResponse.getBody();
    assertThat("HTTP status should be " + httpStatus.getReasonPhrase(), testApiErrorResponse.getStatusCode(),
        equalTo(httpStatus));
    assertNotNull("Body is not returned.", apiError);
    assertThat("Incorrect error code is returned.", apiError.getCode(), startsWith(errorCode));
  }
}
