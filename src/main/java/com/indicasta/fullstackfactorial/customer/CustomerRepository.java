package com.indicasta.fullstackfactorial.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository <Customer, Integer> {
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer customerId);
    Optional<Customer> findCustomerByEmail(String email);
    @Modifying
    @Query("UPDATE Customer customer SET customer.profilePicId = ?2 WHERE customer.id = ?1")
    Integer updateProfilePicId(Integer customerId, String profilePicId);
}
