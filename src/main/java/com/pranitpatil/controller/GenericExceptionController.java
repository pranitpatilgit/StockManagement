package com.pranitpatil.controller;

import com.pranitpatil.dto.ErrorResponse;
import com.pranitpatil.exception.NotFoundException;
import com.pranitpatil.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GenericExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(GenericExceptionController.class);
    private static final String ERROR_RESP_TEXT = "Sending error response - ";


    @ExceptionHandler({NotFoundException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ErrorResponse handleException(RuntimeException exception) {
        logger.error(ERROR_RESP_TEXT, exception);

        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    ErrorResponse handleException(Exception exception) {
        logger.error(ERROR_RESP_TEXT, exception);

        return new ErrorResponse(exception.getMessage());
    }
}
