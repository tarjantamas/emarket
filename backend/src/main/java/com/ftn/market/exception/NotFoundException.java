package com.ftn.market.exception;

public class NotFoundException extends RuntimeException {

  private final String errorCode;

  public NotFoundException(final String errorCode) {
    super();
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
