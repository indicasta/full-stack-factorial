package com.indicasta.fullstackfactorial.exception;

import java.time.LocalDateTime;

/**
 * The type Api error.
 */
public record ApiError(String path,
                       String message,
                       int statusCode,
                       LocalDateTime localDateTime) {
}
