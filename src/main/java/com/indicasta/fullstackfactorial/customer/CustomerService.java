package com.indicasta.fullstackfactorial.customer;

import com.indicasta.fullstackfactorial.auth.RegisterRequest;
import com.indicasta.fullstackfactorial.exception.DuplicateResourceException;
import com.indicasta.fullstackfactorial.exception.ResourceNotFoundException;
import com.indicasta.fullstackfactorial.s3.S3Bucket;
import com.indicasta.fullstackfactorial.s3.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerService {

    private final CustomerDao customerDao;
    private final CustomerDTOMap customerDTOMap;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final S3Bucket s3Bucket;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao, CustomerDTOMap customerDTOMap, PasswordEncoder passwordEncoder, S3Service s3Service, S3Bucket s3Bucket) {
        this.customerDao = customerDao;
        this.customerDTOMap = customerDTOMap;
        this.passwordEncoder = passwordEncoder;
        this.s3Service = s3Service;
        this.s3Bucket = s3Bucket;
    }

    public List<CustomerDTO> getAllCustomers() {
        log.info("Listening all customers in database");
        return customerDao.selectAllCustomers()
                .stream()
                .map(customerDTOMap)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Integer customerId) {
        log.info("Given a customer by ID if it exists");
        return checkWetherCustomerExistsOrThrowException(customerId);
    }

    private CustomerDTO checkWetherCustomerExistsOrThrowException(Integer customerId) {
        return customerDao.selectCustomerById(customerId)
                .map(customerDTOMap)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with ID [%s] not found".formatted(customerId)
                ));
    }

    public void addCustomer(RegisterRequest customerRegistrationRequest) {
        log.info("Adding a new customer");
        // check if email exists
        String email = customerRegistrationRequest.getEmail();
        if (customerDao.existsCustomerWithEmail(email)) {
            log.info("The email provided already exists in the system");
            throw new DuplicateResourceException(
                    "email already taken"
            );
        }
        // add
        Customer customer = Customer.builder()
                .firstname(customerRegistrationRequest.getFirstname())
                .lastname(customerRegistrationRequest.getLastname())
                .password(passwordEncoder.encode(customerRegistrationRequest.getPassword()))
                .email(customerRegistrationRequest.getEmail())
                .age(customerRegistrationRequest.getAge())
                .role(Role.USER)
                .build();
        customerDao.insertCustomer(customer);
        log.info("New customer added with email: {}", customerRegistrationRequest.getEmail());
    }

    public void deleteCustomerById(Integer customerId) {
        if (!customerDao.existsCustomerById(customerId)) {
            log.info("There is no customer with the provided ID:{} in the system", customerId);
            throw new ResourceNotFoundException(
                    "customer with ID [%s] not found".formatted(customerId)
            );
        }
        customerDao.deleteCustomerById(customerId);
        log.info("Customer with the provided ID:{} deleted from the system", customerId);
    }
}
