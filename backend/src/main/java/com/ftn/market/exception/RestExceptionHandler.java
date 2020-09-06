package com.ftn.market.exception;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.dto.ApiError;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

  private final MessageSource messageSource;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException exception) {
    final StringBuilder message = new StringBuilder();
    final List<ObjectError> objectErrors = exception.getBindingResult().getAllErrors();

    for (final ObjectError error : objectErrors) {
      message.append("[");
      if (error instanceof FieldError) {
        message.append(((FieldError) error).getField());
        message.append(" ");
      }
      message.append(error.getDefaultMessage());
      message.append("]");
    }

    return buildErrorResponse(HttpStatus.BAD_REQUEST, message.toString(), ErrorCodes.DTO_VALIDATION_FAILED);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFoundException(final NotFoundException exception) {
    return buildErrorResponse(HttpStatus.NOT_FOUND, null, exception.getErrorCode());
  }

  @ExceptionHandler(NotLoggedInException.class)
  public ResponseEntity<ApiError> handleNotLoggedInException() {
    return buildErrorResponse(HttpStatus.UNAUTHORIZED, null, ErrorCodes.UNAUTHORIZED);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiError> handleBadRequestException(final BadRequestException exception) {
    return buildErrorResponse(HttpStatus.BAD_REQUEST, null, exception.getErrorCode());
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiError> handleBadCredentialsException() {
    return buildErrorResponse(HttpStatus.BAD_REQUEST, null, ErrorCodes.BAD_CREDENTIALS);
  }

  @ExceptionHandler(InternalServerException.class)
  public ResponseEntity<ApiError> handleInternalServerException(final InternalServerException exception) {
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
        "Oops! Something unexpectedly went wrong. Please contact the support to fix this issue.",
        exception.getErrorCode());
  }

  private ResponseEntity<ApiError> buildErrorResponse(final HttpStatus httpStatus, final String explanation,
      final String code) {
    return new ResponseEntity<>(
        new ApiError(messageSource.getMessage(formatErrorCode(code), null, Locale.ENGLISH), explanation, code),
        httpStatus);
  }

  private String formatErrorCode(final String code) {
    return code.contains(".") ? code.split("\\.")[0] : code;
  }
}
