package com.ufmg.testedesoftware.animelistofmine.handler;

import com.ufmg.testedesoftware.animelistofmine.exception.BadRequestException;
import com.ufmg.testedesoftware.animelistofmine.exception.BadRequestExceptionDetails;
import com.ufmg.testedesoftware.animelistofmine.exception.ValidationExceptionDetails;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<BadRequestExceptionDetails> badRequestExceptionHandler(
      BadRequestException badRequestException) {
    BadRequestExceptionDetails badRequestExceptionDetails =
        BadRequestExceptionDetails.builder()
            .title("Bad Request Exception. Please check the documentation.")
            .details(badRequestException.getMessage())
            .developerMessage(badRequestException.getClass().getName())
            .status(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .build();
    return new ResponseEntity<>(badRequestExceptionDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationExceptionDetails> methodArgumentNotValidExceptionHandler(
      MethodArgumentNotValidException methodArgumentNotValidException) {
    List<FieldError> fieldErrors =
        methodArgumentNotValidException.getBindingResult().getFieldErrors();
    String fields =
        fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
    String fieldsMessages =
        fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

    ValidationExceptionDetails validationExceptionDetails =
        ValidationExceptionDetails.builder()
            .title("Bad Request Exception. Required fields not informed.")
            .details("Check the field(s) error(s).")
            .developerMessage(methodArgumentNotValidException.getClass().getName())
            .status(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .fields(fields)
            .fieldsMessages(fieldsMessages)
            .build();

    return new ResponseEntity<>(validationExceptionDetails, HttpStatus.BAD_REQUEST);
  }
}
