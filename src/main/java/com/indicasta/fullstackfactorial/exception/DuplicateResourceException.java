package com.indicasta.fullstackfactorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Duplicate resource exception.
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {
    /**
     * Instantiates a new Duplicate resource exception.
     *
     * @param message the message
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}
