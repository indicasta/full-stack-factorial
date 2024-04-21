package com.indicasta.fullstackfactorial.auth;

public record AuthenticationRequest(String email,
                                    String password) {
}
