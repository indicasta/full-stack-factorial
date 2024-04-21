package com.indicasta.fullstackfactorial.customer;

import com.indicasta.fullstackfactorial.AbstractTestContainer;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerJDBCDataAccessServiceTest extends AbstractTestContainer {

    private CustomerJDBCDataAccessService toTest;
    private final CustomerMapper customerMapper = new CustomerMapper();

    @BeforeEach
    void setUp() {
        toTest = new CustomerJDBCDataAccessService(getJdbcTemplate(),customerMapper);
    }

    @Test
    void selectAllCustomers() {
        //GIVEN
        toTest.insertCustomer(createFakerCustomer());

        //WHEN
        List<Customer> actual = toTest.selectAllCustomers();

        //THEN
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        //GIVEN
        Customer customerToFind = createFakerCustomer();
        String email = customerToFind.getEmail();
        toTest.insertCustomer(customerToFind);
        int idToFind = toTest.selectAllCustomers()
                .stream()
                .filter(e -> e.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //WHEN
        Optional<Customer> customer = toTest.selectCustomerById(idToFind);

        //THEN
        assertThat(customer).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(idToFind);
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getFirstname()).isEqualTo(customerToFind.getFirstname());
            assertThat(c.getLastname()).isEqualTo(customerToFind.getLastname());
            assertThat(c.getAge()).isEqualTo(customerToFind.getAge());
            assertThat(c.getRole()).isEqualTo(customerToFind.getRole());
        });
    }

    @Test
    void insertCustomer() {
        //GIVEN
        Customer customerToInsert = createFakerCustomer();
        String email = customerToInsert.getEmail();

        //WHEN
        toTest.insertCustomer(customerToInsert);
        Optional<Customer> customer = toTest.selectUserByEmail(email);

        //THEN
        assertThat(customer).isPresent().hasValueSatisfying(c->{
            assertThat(c.getEmail()).isEqualTo(email);
        });
    }

    @Test
    void existsCustomerWithEmail() {
        //GIVEN
        Customer customer = createFakerCustomer();
        String email = customer.getEmail();
        toTest.insertCustomer(customer);

        //WHEN
        boolean exists = toTest.existsCustomerWithEmail(email);

        //THEN
        assertThat(exists).isTrue();
    }

    @Test
    void doesNotExistsCustomerWithEmailThenReturnsFalse() {
        //GIVEN
        String email = "obviously@itdoesnotexists.com";

        //WHEN
        boolean exists = toTest.existsCustomerWithEmail(email);

        //THEN
        assertThat(exists).isFalse();
    }

    @Test
    void existsCustomerById() {
        //GIVEN
        Customer customerToFind = createFakerCustomer();
        String email = customerToFind.getEmail();
        toTest.insertCustomer(customerToFind);
        int idToFind = toTest.selectAllCustomers()
                .stream()
                .filter(e -> e.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //WHEN
        boolean exists = toTest.existsCustomerById(idToFind);

        //THEN
        assertThat(exists).isTrue();
    }

    @Test
    void doesNotExistsCustomerWithIdThenReturnsFalse() {
        //GIVEN
        int id = -1;

        //WHEN
        boolean exists = toTest.existsCustomerById(id);

        //THEN
        assertThat(exists).isFalse();
    }

    @Test
    void deleteCustomerById() {
        //GIVEN
        Customer customerToFind = createFakerCustomer();
        String email = customerToFind.getEmail();
        toTest.insertCustomer(customerToFind);
        int idToFind = toTest.selectAllCustomers()
                .stream()
                .filter(e -> e.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //WHEN
        toTest.deleteCustomerById(idToFind);

        //THEN
        Optional<Customer> customer = toTest.selectCustomerById(idToFind);
        assertThat(customer).isNotPresent();
    }

    @Test
    void updateCustomer() {
        //GIVEN
        Customer customer = createFakerCustomer();
        String email = customer.getEmail();

        toTest.insertCustomer(customer);

        int id = toTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //WHEN
        Customer update = new Customer();
        update.setId(id);
        update.setFirstname("Firstname");
        update.setLastname("Lastname");
        String newEmail = "new_email@factorial.com";
        update.setEmail(newEmail);
        update.setAge(40);

        toTest.updateCustomer(update);

        //THEN
        Optional<Customer> actual = toTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(updated -> {
            assertThat(updated.getId()).isEqualTo(id);
            assertThat(updated.getFirstname()).isEqualTo("Firstname");
            assertThat(updated.getLastname()).isEqualTo("Lastname");
            assertThat(updated.getEmail()).isEqualTo(newEmail);
            assertThat(updated.getAge()).isEqualTo(40);
        });
    }

    @Test
    void selectUserByEmail() {
        //GIVEN
        Customer customerToFind = createFakerCustomer();
        String email = customerToFind.getEmail();
        toTest.insertCustomer(customerToFind);

        //WHEN
        Optional<Customer> customer = toTest.selectUserByEmail(email);

        //THEN
        assertThat(customer).isPresent().hasValueSatisfying(c->{
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getFirstname()).isEqualTo(customerToFind.getFirstname());
            assertThat(c.getLastname()).isEqualTo(customerToFind.getLastname());
            assertThat(c.getAge()).isEqualTo(customerToFind.getAge());
            assertThat(c.getRole()).isEqualTo(customerToFind.getRole());
        });
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        //GIVEN
        Customer customer = createFakerCustomer();
        String email = customer.getEmail();

        toTest.insertCustomer(customer);

        int id = toTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //WHEN
        Customer update = new Customer();
        update.setId(id);

        toTest.updateCustomer(update);

        //THEN
        Optional<Customer> actual = toTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getFirstname()).isEqualTo(customer.getFirstname());
            assertThat(c.getLastname()).isEqualTo(customer.getLastname());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }

    @Test
    void updateCustomerProfilePicId() {
        //GIVEN
        Customer customer = createFakerCustomer();
        String email = customer.getEmail();
        String profilePicId = UUID.randomUUID().toString();

        toTest.insertCustomer(customer);

        int id = toTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //WHEN
        toTest.updateCustomerProfilePicId(id, profilePicId);

        //THEN
        Optional<Customer> actual = toTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getFirstname()).isEqualTo(customer.getFirstname());
            assertThat(c.getLastname()).isEqualTo(customer.getLastname());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getProfilePicId()).isEqualTo(profilePicId);
        });
    }

    private Customer createFakerCustomer() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        int age = 39;
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@factorial.com";
        return Customer.builder()
                .firstname(firstName)
                .lastname(lastName)
                .age(age)
                .email(email)
                .password("secret")
                .role(Role.USER)
                .build();
    }
}