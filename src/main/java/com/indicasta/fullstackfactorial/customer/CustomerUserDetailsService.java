package com.indicasta.fullstackfactorial.customer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The type Customer user details service.
 */
@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerDao customerDao;

    /**
     * Instantiates a new Customer user details service.
     *
     * @param customerDao the customer data access object
     */
    public CustomerUserDetailsService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return customerDao.selectUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Username " + email + " not found"));
    }
}
