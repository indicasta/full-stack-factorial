package com.indicasta.fullstackfactorial.customer;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Customer Data Transfer Object mapper.
 * Functional Programming Practice
 */
@Service
public class CustomerDTOMap implements Function<Customer, CustomerDTO> {
    @Override
    public  CustomerDTO apply(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAge(),
                customer.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(Role::valueOf)
                        .collect(Collectors.toList()),
                customer.getProfilePicId()

        );
    }
}
