package com.indicasta.fullstackfactorial.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * The interface Customer repository.
 */
public interface
CustomerRepository extends JpaRepository <Customer, Integer> {
    /**
     * Exists customer by email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    boolean existsCustomerByEmail(String email);

    /**
     * Exists customer by id boolean.
     *
     * @param customerId the customer id
     * @return the boolean
     */
    boolean existsCustomerById(Integer customerId);

    /**
     * Find customer by email optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<Customer> findCustomerByEmail(String email);

    /**
     * Update profile pic id integer.
     *
     * @param customerId   the customer id
     * @param profilePicId the profile pic id
     * @return the integer
     */
    @Modifying
    @Query("UPDATE Customer customer SET customer.profilePicId = ?2 WHERE customer.id = ?1")
    Integer updateProfilePicId(Integer customerId, String profilePicId);
}
