package org.gs.incode.services.stringtransformation.container.exceptionhandler;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.gs.incode.services.stringtransformation.exceptions.InitTransformationServiceException;
import org.gs.incode.services.stringtransformation.exceptions.TransformationServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {
  @ResponseBody
  @org.springframework.web.bind.annotation.ExceptionHandler(Throwable.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleException(Throwable exception) {
    log.error(exception.getMessage(), exception);
    return ErrorResponse.builder()
        .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .message("Unexpected error")
        .build();
  }

  @ResponseBody
  @org.springframework.web.bind.annotation.ExceptionHandler(TransformationServiceException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleException(TransformationServiceException exception) {
    log.error(exception.getMessage(), exception);
    return ErrorResponse.builder()
        .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .message("Unexpected error service exception")
        .build();
  }

  @ResponseBody
  @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(MethodArgumentNotValidException exception) {
    log.error(exception.getMessage(), exception);
    return ErrorResponse.builder()
        .message(joinErrorMsg(exception))
        .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .build();
  }

  @ResponseBody
  @org.springframework.web.bind.annotation.ExceptionHandler(
      InitTransformationServiceException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(InitTransformationServiceException exception) {
    log.error(exception.getMessage(), exception);
    return ErrorResponse.builder()
        .message(exception.getMessage())
        .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .build();
  }

  private static String joinErrorMsg(MethodArgumentNotValidException exception) {
    return exception.getAllErrors().stream()
        .map(
            error -> {
              String name = error.getObjectName();
              if (error instanceof FieldError fe) {
                name = fe.getField();
              }

              return "%s: %s".formatted(name, error.getDefaultMessage());
            })
        .collect(Collectors.joining("|"));
  }
}
