package com.indicasta.fullstackfactorial.auth;

import com.indicasta.fullstackfactorial.config.JwtService;
import com.indicasta.fullstackfactorial.customer.Customer;
import com.indicasta.fullstackfactorial.customer.CustomerRepository;
import com.indicasta.fullstackfactorial.customer.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
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
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        Customer customer = repository.findCustomerByEmail(request.getEmail())
                .orElseThrow();
        return getAuthenticationResponse(customer);
    }
}
