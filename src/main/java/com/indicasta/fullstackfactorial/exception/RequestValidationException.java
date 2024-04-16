package com.indicasta.fullstackfactorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Request validation exception.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class RequestValidationException extends RuntimeException{
    /**
     * Instantiates a new Request validation exception.
     *
     * @param message the message
     */
    public RequestValidationException(String message) {
        super(message);
    }
}
