package com.indicasta.fullstackfactorial.auth;

import com.indicasta.fullstackfactorial.customer.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO
) {
}
