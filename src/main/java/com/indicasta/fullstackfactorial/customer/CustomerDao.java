package com.indicasta.fullstackfactorial.customer;

import java.util.List;
import java.util.Optional;

/**
 * The interface Customer dao.
 */
public interface CustomerDao {
    /**
     * Select all customers list.
     *
     * @return the list
     */
    List<Customer> selectAllCustomers();

    /**
     * Select customer by id optional.
     *
     * @param customerId the customer id
     * @return the optional
     */
    Optional<Customer> selectCustomerById(Integer customerId);

    /**
     * Insert customer.
     *
     * @param customer the customer
     */
    void insertCustomer(Customer customer);

    /**
     * Exists customer with email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    boolean existsCustomerWithEmail(String email);

    /**
     * Exists customer by id boolean.
     *
     * @param customerId the customer id
     * @return the boolean
     */
    boolean existsCustomerById(Integer customerId);

    /**
     * Delete customer by id.
     *
     * @param customerId the customer id
     */
    void deleteCustomerById(Integer customerId);

    /**
     * Update customer.
     *
     * @param update the update
     */
    void updateCustomer(Customer update);

    /**
     * Select user by email optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<Customer> selectUserByEmail(String email);

    /**
     * Update customer profile pic id.
     *
     * @param customerId   the customer id
     * @param profilePicId the profile pic id
     */
    void updateCustomerProfilePicId(Integer customerId, String profilePicId);
}
