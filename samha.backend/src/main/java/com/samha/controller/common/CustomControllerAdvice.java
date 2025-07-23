package com.samha.controller.common;

import com.samha.commons.BusinessException;
import com.samha.commons.ErrorResponse;
import com.samha.commons.UnexpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
class CustomControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomControllerAdvice.class);

    @ExceptionHandler(UnexpectedException.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        LOGGER.error(e.getMessage(), e);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();

        return new ResponseEntity<>(
                new ErrorResponse(
                        status,
                        e.getMessage(),
                        stackTrace
                ),
                status
        );
    }


    @ExceptionHandler({BusinessException.class, NullPointerException.class})
    public ResponseEntity<ErrorResponse> handleInternalException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();

        return new ResponseEntity<>(
                new ErrorResponse(
                        status,
                        e.getMessage(),
                        stackTrace // specifying the stack trace in case of 500s
                ),
                status
        );
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        LOGGER.error(e.getMessage(), e);
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();

        return new ResponseEntity<>(
                new ErrorResponse(
                        status,
                        "Este registro não pode ser excluído, pois, possui outros registros associados a ele!",
                        stackTrace // specifying the stack trace in case of 500s
                ),
                status
        );
    }
}
