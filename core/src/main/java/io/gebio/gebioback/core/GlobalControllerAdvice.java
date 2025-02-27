package io.gebio.gebioback.core;

import io.gebio.gebioback.core.exception.BoardNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler({ BoardNotFound.class })
  ProblemDetail handleBoardNotFound(BoardNotFound ex) {
    return ProblemDetail.forStatusAndDetail(
      HttpStatus.NOT_FOUND,
      ex.getMessage()
    );
  }
}
