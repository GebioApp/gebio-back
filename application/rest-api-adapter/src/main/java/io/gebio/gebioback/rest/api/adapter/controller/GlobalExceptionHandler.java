package io.gebio.gebioback.rest.api.adapter.controller;

import io.gebio.gebioback.contract.model.UnauthorizedResponseContract;
import io.gebio.gebioback.domain.exception.InvalidJwtToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidJwtToken.class)
  protected ResponseEntity<UnauthorizedResponseContract> handleInvalidJwtToken(
    InvalidJwtToken ex
  ) {
    UnauthorizedResponseContract unauthorizedResponseContract =
      new UnauthorizedResponseContract();
    unauthorizedResponseContract.setDescription(ex.getMessage());
    return ResponseEntity.status(401).body(unauthorizedResponseContract);
  }
}
