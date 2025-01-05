package org.bazaar.giza.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.bazaar.giza.constant.ErrorMessage;
import org.bazaar.giza.user.entity.BazaarUser;
import org.bazaar.giza.user.exception.BazaarUserException;
import org.bazaar.giza.user.repo.BazaarUserRepo;
import org.bazaar.giza.user.service.BazaarUserService;
import org.bazaar.giza.user.service.BazaarUserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class BazaarUserServiceTest {
    @TestConfiguration
    static class ServiceTestConfig {
        @Bean
        BazaarUserService service(BazaarUserRepo bazaarUserRepo) {
            return new BazaarUserServiceImpl(bazaarUserRepo);
        }
    }

    @MockitoBean
    private BazaarUserRepo bazaarUserRepo;

    @Autowired
    private BazaarUserService bazaarUserService;

    private static BazaarUser bazaarUser;
    private static BazaarUser invalidBazaarUser;
    private static List<BazaarUser> bazaarUsers;

    @BeforeAll
    public static void setUp() {
        bazaarUser = new BazaarUser();
        bazaarUser.setUserId(1L);
        bazaarUser.setFirstName("Bob");
        bazaarUser.setLastName("Ferguson");
        bazaarUser.setEmail("Bob@gmail.com");
        bazaarUser.setPhoneNumber("01002993382");
        bazaarUser.setPassword("password");

        invalidBazaarUser = new BazaarUser(
                2L,
                bazaarUser.getFirstName(),
                bazaarUser.getLastName(),
                bazaarUser.getEmail() + "1",
                bazaarUser.getPhoneNumber() + "1",
                "password");

        bazaarUsers = List.of(bazaarUser);
    }

    @BeforeEach
    public void setUpForEach() {
        when(bazaarUserRepo.findById(bazaarUser.getUserId())).thenReturn(Optional.of(bazaarUser));
        when(bazaarUserRepo.findById(invalidBazaarUser.getUserId())).thenReturn(Optional.empty());

        when(bazaarUserRepo.findByEmail(bazaarUser.getEmail())).thenReturn(Optional.empty());
        when(bazaarUserRepo.findByPhoneNumber(bazaarUser.getPhoneNumber())).thenReturn(Optional.empty());

        when(bazaarUserRepo.findAll()).thenReturn(bazaarUsers);

        when(bazaarUserRepo.save(bazaarUser)).thenReturn(bazaarUser);
    }

    @Test
    public void createUser_DuplicateEmail() {
        when(bazaarUserRepo.findByEmail(invalidBazaarUser.getEmail())).thenReturn(Optional.of(invalidBazaarUser));

        BazaarUserException ex = assertThrows(BazaarUserException.class,
                () -> {
                    bazaarUserService.createUser(invalidBazaarUser);
                });
        assertEquals(ErrorMessage.DUPLICATE_EMAIL, ex.getMessage());
    }

    @Test
    public void createUser_DuplicatePhoneNumber() {
        when(bazaarUserRepo.findByPhoneNumber(invalidBazaarUser.getPhoneNumber()))
                .thenReturn(Optional.of(invalidBazaarUser));

        BazaarUserException ex = assertThrows(BazaarUserException.class,
                () -> {
                    bazaarUserService.createUser(invalidBazaarUser);
                });
        assertEquals(ErrorMessage.DUPLICATE_PHONE_NUMBER, ex.getMessage());
    }

    @Test
    public void createUser_DuplicateEmailAndPhoneNumber() {
        when(bazaarUserRepo.findByEmail(invalidBazaarUser.getEmail())).thenReturn(Optional.of(invalidBazaarUser));
        when(bazaarUserRepo.findByPhoneNumber(invalidBazaarUser.getPhoneNumber()))
                .thenReturn(Optional.of(invalidBazaarUser));

        BazaarUserException ex = assertThrows(BazaarUserException.class,
                () -> {
                    bazaarUserService.createUser(invalidBazaarUser);
                });
        assertTrue(ex.getMessage().contains(ErrorMessage.DUPLICATE_EMAIL));
        assertTrue(ex.getMessage().contains(ErrorMessage.DUPLICATE_PHONE_NUMBER));
    }

    @Test
    public void createUser_Valid() {
        assertEquals(bazaarUser, bazaarUserService.createUser(bazaarUser));
    }

    @Test
    public void getSingleUser_NonExistant() {
        BazaarUserException ex = assertThrows(BazaarUserException.class,
                () -> {
                    bazaarUserService.getSingleUser(invalidBazaarUser.getUserId());
                });
        assertEquals(ErrorMessage.USER_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    public void getSingleUser_Existant() {
        assertEquals(bazaarUser, bazaarUserService.getSingleUser(bazaarUser.getUserId()));
    }

    @Test
    public void deleteUser_NonExistant() {
        BazaarUserException ex = assertThrows(BazaarUserException.class,
                () -> {
                    bazaarUserService.deleteUser(invalidBazaarUser.getUserId());
                });
        assertEquals(ErrorMessage.USER_ID_NOT_FOUND, ex.getMessage());

        verify(bazaarUserRepo, times(0)).delete(any());
    }

    @Test
    public void deleteUser_Existant() {
        assertEquals("User deleted Successfully.", bazaarUserService.deleteUser(bazaarUser.getUserId()));

        verify(bazaarUserRepo, times(1)).delete(bazaarUser);
    }
}
