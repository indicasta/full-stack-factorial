package com.indicasta.fullstackfactorial.auth;

import com.indicasta.fullstackfactorial.config.JWTService;
import com.indicasta.fullstackfactorial.customer.Customer;
import com.indicasta.fullstackfactorial.customer.CustomerDTOMap;
import com.indicasta.fullstackfactorial.customer.CustomerRepository;
import com.indicasta.fullstackfactorial.customer.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The type Authentication service.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomerDTOMap customerDTOMap;

    /**
     * Register authentication response.
     *
     * @param request the request
     * @return the authentication response
     */
    public AuthenticationResponse register(RegisterRequest request) {

        Customer customer = Customer
                .builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .age(request.getAge())
                .role(Role.USER)
                .build();
        repository.save(customer);
        return getAuthenticationResponse(customer);

    }

    private AuthenticationResponse getAuthenticationResponse(Customer customer) {
        String jwtToken = jwtService.generateToken(customer);
        return new AuthenticationResponse(jwtToken, customerDTOMap.apply(customer));
    }

    /**
     * Authenticate authentication response.
     *
     * @param request the request
     * @return the authentication response
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        ));
        Customer customer = repository.findCustomerByEmail(request.email())
                .orElseThrow();
        String token = jwtService.generateToken(customer);
        return new AuthenticationResponse(token, customerDTOMap.apply(customer));
    }
}
