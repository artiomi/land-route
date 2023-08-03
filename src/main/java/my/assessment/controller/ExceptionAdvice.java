package my.assessment.controller;

import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import my.assessment.exception.RouteCalculationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

  @ExceptionHandler({RouteCalculationException.class, ConstraintViolationException.class})
  public ResponseEntity<Map<String, String>> handleException(Throwable cause) {
    log.error("Route calculation failed.", cause);
    return new ResponseEntity<>(Map.of("message", cause.getMessage()), HttpStatus.BAD_REQUEST);
  }

}
