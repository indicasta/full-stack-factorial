package com.indicasta.fullstackfactorial.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The type Customer jpa data access service.
 */
@Slf4j
@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao{

    private final CustomerRepository customerRepository;

    /**
     * Instantiates a new Customer jpa data access service.
     *
     * @param customerRepository the customer repository
     */
    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        Page<Customer> page = customerRepository.findAll(Pageable.ofSize(100));
        log.info("Showing all customers 100 by 100");
        return page.getContent();
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        log.info("Getting the customer by id {}", customerId);
        return customerRepository.findById(customerId);
    }

    @Override
    public void insertCustomer(Customer customer) {
        log.info("Insert new customer with email: {}", customer.getEmail());
        customerRepository.save(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        log.info("Verifying the existence of customer with email: {}", email);
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public boolean existsCustomerById(Integer customerId) {
        log.info("Verifying the existence of customer with id: {}", customerId);
        return customerRepository.existsCustomerById(customerId);
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        log.info("Deleting customer with id: {}", customerId);
        customerRepository.deleteById(customerId);
    }

    /*Uses EntityManager merge() method*/
    @Override
    public void updateCustomer(Customer update) {
        log.info("Updating customer with id: {}", update.getId());
        customerRepository.save(update);
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        log.info("Getting customer with email: {}", email);
        return customerRepository.findCustomerByEmail(email);
    }

    @Override
    public void updateCustomerProfilePicId(Integer customerId, String profilePicId) {
        log.info("Updating profile picture Id to : {}, for customer with id: {}", profilePicId, customerId);
        customerRepository.updateProfilePicId(customerId,profilePicId);
    }
}
