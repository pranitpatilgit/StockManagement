package com.pranitpatil.controller;

import com.pranitpatil.dto.ErrorResponse;
import com.pranitpatil.exception.NotFoundException;
import com.pranitpatil.exception.StockLockedException;
import com.pranitpatil.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GenericExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(GenericExceptionController.class);
    private static final String ERROR_RESP_TEXT = "Sending error response - ";


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ErrorResponse handleException(ValidationException exception) {
        return getErrorResponse(exception);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    ErrorResponse handleException(NotFoundException exception) {
        return getErrorResponse(exception);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ErrorResponse handleException(MethodArgumentNotValidException exception) {
        logger.error(ERROR_RESP_TEXT, exception);
        StringBuilder builder = new StringBuilder();
        for(ObjectError error : exception.getBindingResult().getAllErrors()){
            builder.append(error.getDefaultMessage())
                    .append(" | ");
        }
        return new ErrorResponse(builder.toString());
    }

    @ExceptionHandler({StockLockedException.class})
    @ResponseStatus(HttpStatus.LOCKED)
    public @ResponseBody
    ErrorResponse handleException(StockLockedException exception) {
        return getErrorResponse(exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    ErrorResponse handleException(Exception exception) {
        return getErrorResponse(exception);
    }

    private ErrorResponse getErrorResponse(Exception exception) {
        logger.error(ERROR_RESP_TEXT, exception);
        return new ErrorResponse(exception.getMessage());
    }
}
