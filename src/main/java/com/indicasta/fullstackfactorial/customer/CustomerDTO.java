package com.indicasta.fullstackfactorial.customer;

import java.util.List;

/**
 * The type Customer Data Transfer Object, as record to have it clean and simple.
 */
public record CustomerDTO(
        Integer id,
        String firstname,
        String lastname,
        String email,
        Integer age,
        List<Role> roles,
        String profilePicId
) {
}
