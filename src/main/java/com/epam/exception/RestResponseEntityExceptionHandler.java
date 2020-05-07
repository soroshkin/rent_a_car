package com.epam.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LogManager.getLogger();
    private static final HttpHeaders headers = new HttpHeaders();

    static {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        LOG.error(ex.getMessage());
        String bodyOfResponse = "No data presents";
        return new ResponseEntity<>(bodyOfResponse, headers, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        LOG.error(ex.getMessage());
        String bodyOfResponse = "Entity not found";
        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = JsonProcessingException.class)
    protected ResponseEntity<Object> handleJSONProcessingException(Exception ex, WebRequest request) {
        LOG.error(ex.getMessage());
        String bodyOfResponse = "Incorrect JSON string";
        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    protected ResponseEntity<Object> handleFileNotFound(Exception ex, WebRequest request) {
        LOG.error(ex.getMessage());
        String bodyOfResponse = "File not found";
        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class,
            ConstraintViolationException.class})
    protected ResponseEntity<Object> handleDataIntegrityViolation(RuntimeException ex, WebRequest request) {
        LOG.error(ex.getMessage());
        String bodyOfResponse = ex.getMessage();

        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class,
            InvalidDataAccessApiUsageException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(RuntimeException ex, WebRequest request) {
        LOG.error(ex.getMessage());
        String bodyOfResponse = "Illegal argument";
        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }
}
