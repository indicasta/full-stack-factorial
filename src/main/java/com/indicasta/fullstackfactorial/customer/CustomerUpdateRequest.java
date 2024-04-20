package com.indicasta.fullstackfactorial.customer;

public record CustomerUpdateRequest(
        String firstname,
        String lastname,
        String email,
        Integer age
) {
}
