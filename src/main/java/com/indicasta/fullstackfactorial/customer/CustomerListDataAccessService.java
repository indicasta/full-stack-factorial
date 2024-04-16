package com.indicasta.fullstackfactorial.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The type Customer list data access service.
 */
@Slf4j
@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {

    private static final List<Customer> customers;

    static {
        customers =new ArrayList<>();

        Customer indi = new Customer(
                1,
                "Indira",
                "Castañeda",
                "indicasta@gmail.com",
                39,
                "secret",
                null,
                Role.USER
                );
        customers.add(indi);
        log.info("Adding customer with email {}", indi.getEmail());

        Customer aleks = new Customer(
                2,
                "Aleks",
                "Kudimenko",
                "aleks.kudimenko@gmail.com",
                35,
                "secret",
                null,
                Role.USER
        );
        customers.add(aleks);
        log.info("Adding customer with email {}", aleks.getEmail());
    }

    @Override
    public List<Customer> selectAllCustomers() {
        log.info("Listing all Customers in Database");
        return customers; }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        log.info("Looking for customer with id: {}", customerId);
        return customers.stream().findFirst().filter(customer -> customer.getId().equals(customerId));
    }

    @Override
    public void insertCustomer(Customer customer) {
        log.info("Registering new customer with email: {}", customer.getEmail());
        customers.add(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        log.info("Looking for customer with email: {}", email);
        return customers.stream().anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public boolean existsCustomerById(Integer customerId) {
        log.info("Verifying the existences of customer with id: {}", customerId);
        return customers.stream().anyMatch(customer -> customer.getId().equals(customerId));
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        log.info("Deleting customer with id: {}", customerId);
        customers.stream().filter(customer -> customer.getId().equals(customerId))
                .findFirst().ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer update) {
        log.info("Updating customer with id: {}", update.getId());
        int index = customers.indexOf(customers.stream()
                .filter(customer -> customer.getId().equals(update.getId()))
                .findFirst().orElse(null));
        if (index != -1) {
            customers.set(index, update);
        }
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        log.info("Getting customer by email: {}", email);
        return customers.stream().filter(customer -> customer.getEmail().equals(email)).findFirst();
    }

    @Override
    public void updateCustomerProfilePicId(Integer customerId, String profilePicId) {
        log.info("Updating profile picture Id to : {}, for customer with id: {}", profilePicId, customerId);
        selectCustomerById(customerId).ifPresent(customer -> customer.setProfilePicId(profilePicId));
    }


}
