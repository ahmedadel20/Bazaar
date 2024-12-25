package org.bazaar.giza;

import java.util.HashSet;
import java.util.Set;

import org.bazaar.giza.auth.entity.UserRoles;
import org.bazaar.giza.auth.repo.UserRolesRepo;
import org.bazaar.giza.constant.Roles;
import org.bazaar.giza.user.entity.BazaarUser;
import org.bazaar.giza.user.repo.BazaarUserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
@Transactional
public class DataInitializer implements CommandLineRunner {
    private final BazaarUserRepo bazaarUserRepo;
    private final UserRolesRepo userRolesRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        BazaarUser user = new BazaarUser();
        user.setFirstName("Bob");
        user.setLastName("Ferguson");
        user.setEmail("Bob@gmail.com");
        user.setPhoneNumber("01002993382");
        user.setPassword(passwordEncoder.encode("password"));
        user = bazaarUserRepo.save(user);

        // Re-fetch the user to ensure it's attached
        user = bazaarUserRepo.findById(user.getUserId()).orElseThrow();

        Set<String> roles = new HashSet<>();
        roles.add(Roles.ADMIN);
        roles.add(Roles.MANAGER);
        UserRoles userRoles = new UserRoles();
        userRoles.setUser(user);
        userRoles.setRoles(roles);
        userRolesRepo.save(userRoles);
    }
}
