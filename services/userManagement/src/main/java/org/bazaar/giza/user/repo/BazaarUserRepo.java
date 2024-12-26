package org.bazaar.giza.user.repo;

import java.util.Optional;

import org.bazaar.giza.user.entity.BazaarUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BazaarUserRepo extends JpaRepository<BazaarUser, Long> {
    Optional<BazaarUser> findByEmail(String email);

    Optional<BazaarUser> findByPhoneNumber(String phoneNumber);
}
