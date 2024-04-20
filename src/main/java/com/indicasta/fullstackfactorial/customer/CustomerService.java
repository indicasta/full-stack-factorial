package com.indicasta.fullstackfactorial.customer;

import com.indicasta.fullstackfactorial.auth.RegisterRequest;
import com.indicasta.fullstackfactorial.exception.DuplicateResourceException;
import com.indicasta.fullstackfactorial.exception.RequestValidationException;
import com.indicasta.fullstackfactorial.exception.ResourceNotFoundException;
import com.indicasta.fullstackfactorial.s3.S3Bucket;
import com.indicasta.fullstackfactorial.s3.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.utils.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The type Customer service.
 */
@Service
@Slf4j
public class CustomerService {

    private final CustomerDao customerDao;
    private final CustomerDTOMap customerDTOMap;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final S3Bucket s3Bucket;

    /**
     * Instantiates a new Customer service.
     *
     * @param customerDao     the customer dao
     * @param customerDTOMap  the customer dto mapper
     * @param passwordEncoder the password encoder
     * @param s3Service       the s 3 service
     * @param s3Bucket        the s 3 bucket
     */
    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao, CustomerDTOMap customerDTOMap, PasswordEncoder passwordEncoder, S3Service s3Service, S3Bucket s3Bucket) {
        this.customerDao = customerDao;
        this.customerDTOMap = customerDTOMap;
        this.passwordEncoder = passwordEncoder;
        this.s3Service = s3Service;
        this.s3Bucket = s3Bucket;
    }

    /**
     * Gets all customers.
     *
     * @return the all customers
     */
    public List<CustomerDTO> getAllCustomers() {
        log.info("Listening all customers in database");
        return customerDao.selectAllCustomers()
                .stream()
                .map(customerDTOMap)
                .collect(Collectors.toList());
    }

    /**
     * Gets customer.
     *
     * @param customerId the customer id
     * @return the customer
     */
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

    /**
     * Add customer.
     *
     * @param customerRegistrationRequest the customer registration request
     */
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

    /**
     * Delete customer by id.
     *
     * @param customerId the customer id
     */
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

    private Customer getCustomerById(Integer customerId) {
        log.info("Searching Customer with the provided ID:{}", customerId);
        return customerDao.selectCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with id [%s] not found".formatted(customerId)
                ));
    }

    /**
     * Update customer.
     *
     * @param customerId            the customer id
     * @param customerUpdateRequest the customer update request
     */
    public void updateCustomer(Integer customerId,
                               CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = getCustomerById(customerId);
        boolean changes = false;

        if (!customerUpdateRequest.firstname().isBlank() && !customerUpdateRequest.firstname().equals(customer.getFirstname())) {
            log.info("Customer's firstname with the provided ID:{}  has changed", customerId);
            customer.setFirstname(customerUpdateRequest.firstname());
            changes = true;
        }

        if (!customerUpdateRequest.lastname().isBlank() && !customerUpdateRequest.lastname().equals(customer.getLastname())) {
            log.info("Customer's lastname with the provided ID:{} has changed", customerId);
            customer.setLastname(customerUpdateRequest.lastname());
            changes = true;
        }

        if (customerUpdateRequest.age()>=18  && customerUpdateRequest.age()<=75 && !customerUpdateRequest.age().equals(customer.getAge())) {
            log.info("Customer's age with the provided ID:{} has changed", customerId);
            customer.setAge(customerUpdateRequest.age());
            changes = true;
        }

        if (!customerUpdateRequest.email().isBlank() && !customerUpdateRequest.email().equals(customer.getEmail())) {
            if (customerDao.existsCustomerWithEmail(customerUpdateRequest.email())) {
                log.info("Customer's email:{} already exits in the system", customerUpdateRequest.email());
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
            log.info("Customer's email with the provided ID:{} has changed", customerId);
            customer.setEmail(customerUpdateRequest.email());
            changes = true;
        }

        if (!changes) {
            log.info("There is nothing to update");
            throw new RequestValidationException("no data changes found");
        }

        customerDao.updateCustomer(customer);
    }

    /**
     * Upload customer profile pic.
     *
     * @param customerId    the customer id
     * @param multipartFile the multipart file
     */
    public void uploadCustomerProfilePic(Integer customerId, MultipartFile multipartFile) {
        checkWetherCustomerExistsOrThrowException(customerId);
        String profilePicId = UUID.randomUUID().toString();
        try {
            s3Service.putObjectIntoS3(s3Bucket.getCustomer(),
                    "profile-images/%s/%s".formatted(customerId, profilePicId),
                    multipartFile.getBytes());

        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
        customerDao.updateCustomerProfilePicId(customerId, profilePicId);
    }

    /**
     * Download customer profile pic byte [ ].
     *
     * @param customerId the customer id
     * @return the byte [ ]
     */
    public byte[] downloadCustomerProfilePic(Integer customerId) {
        CustomerDTO customer = checkWetherCustomerExistsOrThrowException(customerId);
        if(!StringUtils.isBlank(customer.profilePicId())) {
            return s3Service.getObjectFromS3(s3Bucket.getCustomer(),
                    "profile-images/%s/%s".formatted(customerId, customer.profilePicId()));
        }
        else { throw new ResourceNotFoundException(
                "customer with id [%s] profile picture not found".formatted(customerId)); }
    }
}
