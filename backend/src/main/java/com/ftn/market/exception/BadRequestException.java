package com.ftn.market.exception;

public class BadRequestException extends RuntimeException {

  private final String errorCode;

  public BadRequestException(final String errorCode) {
    super();
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
