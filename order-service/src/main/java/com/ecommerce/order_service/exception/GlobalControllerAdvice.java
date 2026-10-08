package com.ecommerce.order_service.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.warn("Resource not found- Path: {}, Message: {}", request.getDescription(false), ex.getMessage());

        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());



         problemDetail.setTitle("Resource not found");
         problemDetail.setType(URI.create("https://api.ecommerce.com/errors/not-found"));
         problemDetail.setProperty("Timestamp", Instant.now());

         problemDetail.setProperty("Resource", ex.getResourceName());
         problemDetail.setProperty("Resource", ex.getFieldName());
         problemDetail.setProperty("Resource", ex.getFieldValue());

         return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {

        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed on one or more fields");

        problemDetail.setTitle("Validation Failed");
        problemDetail.setType(URI.create("https://api.ecommerce.com/errors/validation-failed"));
        problemDetail.setProperty("Timestamp", Instant.now());

        Map<String, String> errorMap= new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((fieldError) -> {errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());});

        problemDetail.setProperty("Errors", errorMap);

        return problemDetail;

    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex, WebRequest request) {

        log.warn("Unexpected Error {}:  {}", request.getDescription(false), ex);

        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error");

        problemDetail.setTitle("Internal server error");
        problemDetail.setType(URI.create("https://api.ecommerce.com/errors/internal"));
        problemDetail.setProperty("Timestamp", Instant.now());


        return problemDetail;

    }
}
