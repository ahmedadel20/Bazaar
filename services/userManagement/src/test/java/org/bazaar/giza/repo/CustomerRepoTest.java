package org.bazaar.giza.repo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.bazaar.giza.customer.entity.Customer;
import org.bazaar.giza.customer.repo.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class CustomerRepoTest {
    @Autowired
    private CustomerRepository repo;

    private static Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setFirstName("Bob");
        customer.setLastName("Ferguson");
        customer.setEmail("Bob@gmail.com");
        customer.setPhoneNumber("01002993382");
        customer.setAddresses(new HashSet<>());

        customer = repo.save(customer);
    }

    @AfterEach
    public void tearDownAfterEach() {
        repo.deleteAll();
    }

    @Test
    public void findByEmail_NonExistant() {
        assertTrue(repo.findByEmail("Blah").isEmpty());
    }

    @Test
    public void findByEmail_Existant() {
        assertTrue(repo.findByEmail(customer.getEmail()).isPresent());
    }

    @Test
    public void findByPhoneNumber_NonExistant() {
        assertTrue(repo.findByPhoneNumber("Blah").isEmpty());
    }

    @Test
    public void findByPhoneNumber_Existant() {
        assertTrue(repo.findByPhoneNumber(customer.getPhoneNumber()).isPresent());
    }
}
