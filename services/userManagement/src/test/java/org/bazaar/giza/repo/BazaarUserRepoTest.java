package org.bazaar.giza.repo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bazaar.giza.user.entity.BazaarUser;
import org.bazaar.giza.user.repo.BazaarUserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class BazaarUserRepoTest {
    @Autowired
    private BazaarUserRepo repo;

    private BazaarUser bazaarUser;

    @BeforeEach
    public void setUp() {
        bazaarUser = new BazaarUser();
        bazaarUser.setFirstName("Bob");
        bazaarUser.setLastName("Ferguson");
        bazaarUser.setEmail("Bob@gmail.com");
        bazaarUser.setPhoneNumber("01002993382");

        bazaarUser = repo.save(bazaarUser);
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
        assertTrue(repo.findByEmail(bazaarUser.getEmail()).isPresent());
    }

    @Test
    public void findByPhoneNumber_NonExistant() {
        assertTrue(repo.findByPhoneNumber("Blah").isEmpty());
    }

    @Test
    public void findByPhoneNumber_Existant() {
        assertTrue(repo.findByPhoneNumber(bazaarUser.getPhoneNumber()).isPresent());
    }
}
