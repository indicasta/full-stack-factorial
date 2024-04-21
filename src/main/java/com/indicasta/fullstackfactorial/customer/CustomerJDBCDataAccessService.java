package com.indicasta.fullstackfactorial.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The type Customer jdbc data access service.
 */
@Slf4j
@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerMapper customerMapper;

    /**
     * Instantiates a new Customer jdbc data access service.
     *
     * @param jdbcTemplate   the jdbc template
     * @param customerMapper the customer mapper
     */
    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerMapper customerMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerMapper = customerMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        log.info("Listing all Customers in Database 100 by 100");
        String sql = """
                SELECT id, firstname, lastname, email, age, password, role, profile_pic_id
                FROM customer
                LIMIT 100
                """;
        return jdbcTemplate.query(sql, customerMapper);

    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        log.info("Looking for customer with id: {}", customerId);
        String sql = """
                SELECT id, firstname, lastname, email, age, password, role, profile_pic_id
                FROM customer
                WHERE id=?
                """;
        return jdbcTemplate.query(sql, customerMapper, customerId)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        log.info("Registering new customer with email: {}", customer.getEmail());
        String sql = """
                INSERT INTO customer (firstname, lastname, email, age, password, role, profile_pic_id)
                VALUES (?,?,?,?,?,?,?)
                """;
        int result = jdbcTemplate.update(
                sql,
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAge(),
                customer.getPassword(),
                customer.getRole().name(),
                customer.getProfilePicId()
        );

        log.info("Inserted customer at position: {}", result);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        log.info("Verifying the existence of customer with email: {}", email);
        String sql = """
                SELECT id, firstname, lastname, email, age, password, role, profile_pic_id
                FROM customer
                WHERE email=?
                """;
        return jdbcTemplate.query(sql, customerMapper, email)
                .stream()
                .findFirst().isPresent();
    }

    @Override
    public boolean existsCustomerById(Integer customerId) {
        log.info("Verifying the existence of customer with id: {}", customerId);
        String sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, customerId);
        return count != null && count > 0;
    }

    /*If the row with the id customerId does not exist, the DELETE statement returns 0 */
    @Override
    public void deleteCustomerById(Integer customerId) {
        log.info("Deleting customer with id: {}", customerId);
            String sql = """
                
                DELETE FROM customer
                WHERE id = ?
                """;
            int result = jdbcTemplate.update(sql, customerId);
            log.info("deleteCustomerById result = {}",result);

    }

    @Override
    public void updateCustomer(Customer update) {
        int result = 0;
        if (update.getFirstname()!=null && !update.getFirstname().isBlank())
        {
            String sql = """
                UPDATE customer
                SET firstname = ?
                WHERE id = ?
                """;
         result = jdbcTemplate.update(
                sql,
                update.getFirstname(),
                update.getId()
        );
        log.info("update customer with id {} firstname result = {}", update.getId(), result);
        }
        if (update.getLastname()!=null && !update.getLastname().isBlank())
        {
            String sql = """
                UPDATE customer
                SET lastname = ?
                WHERE id = ?
                """;
            result = jdbcTemplate.update(
                    sql,
                    update.getLastname(),
                    update.getId()
            );
            log.info("update customer with id {} lastname result = {}", update.getId(), result);
        }
        if (update.getEmail()!=null && !update.getEmail().isBlank())
        {
            String sql = """
                UPDATE customer
                SET email = ?
                WHERE id = ?
                """;
            result = jdbcTemplate.update(
                    sql,
                    update.getEmail(),
                    update.getId()
            );
            log.info("update customer with id {} email result = {}", update.getId(), result);
        }
        if (update.getAge()!=null && update.getAge()>= 18 && update.getAge()<=75)
        {
            String sql = """
                UPDATE customer
                SET age = ?
                WHERE id = ?
                """;
            result = jdbcTemplate.update(
                    sql,
                    update.getAge(),
                    update.getId()
            );
            log.info("update customer with id {} email result = {}", update.getId(), result);
        }
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        log.info("Getting customer with email: {}", email);
        String sql = """
                SELECT id, firstname, lastname, email, password, age, role, profile_pic_id
                FROM customer
                WHERE email = ?
                """;
        return jdbcTemplate.query(sql, customerMapper, email)
                .stream()
                .findFirst();
    }

    @Override
    public void updateCustomerProfilePicId(Integer customerId, String profilePicId) {
        String sql = """
                UPDATE customer
                SET profile_pic_id = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, profilePicId, customerId);
        log.info("Updating profile picture Id to : {}, for customer with id: {}", profilePicId, customerId);
    }
}
