package com.practice.employee.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class RestExceptionAdvisor {
  private final static String DEFAULT_MESSAGE = "시스템 담당자에게 문의 바랍니다.";
  private final static String BINDING_ERROR_MESSAGE = "잘못된 요청입니다.";
  private final static String VIOLATION_ERROR_MESSAGE = "이미 존재하는 이메일 또는 연락처 입니다.";

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> badRequestHandler(RuntimeException e) {
    log.warn(
      "RuntimeException: " + e.getMessage(),
      e
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(e.getMessage() == null ? DEFAULT_MESSAGE : e.getMessage());
  }

  @ExceptionHandler({
    HttpRequestMethodNotSupportedException.class,
    HttpMediaTypeNotSupportedException.class,
    HttpMediaTypeNotAcceptableException.class,
    MissingPathVariableException.class,
    MissingServletRequestParameterException.class,
    ServletRequestBindingException.class,
    ConversionNotSupportedException.class,
    TypeMismatchException.class,
    HttpMessageNotReadableException.class,
    HttpMessageNotWritableException.class,
    MethodArgumentNotValidException.class,
    MissingServletRequestPartException.class,
    BindException.class,
    NoHandlerFoundException.class,
    AsyncRequestTimeoutException.class,
    DataIntegrityViolationException.class
  })
  public ResponseEntity<?> badRequestExceptionHandler(Exception e) {
    var message = DEFAULT_MESSAGE;

    log.warn(
      "Bad Request Exception: " + e.getMessage(),
      e
    );

    if (e instanceof BindingResult) {
      message = BINDING_ERROR_MESSAGE;
    }

    if (e instanceof  DataIntegrityViolationException) {
      message = VIOLATION_ERROR_MESSAGE;
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(message);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<?> exceptionHandler(Throwable e) {
    log.error(
      "Exception: " + e.getMessage(),
      e
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(DEFAULT_MESSAGE);
  }
}
