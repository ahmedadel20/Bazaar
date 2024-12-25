package org.bazaar.paymentGateway.payment.repo;

import org.bazaar.paymentGateway.payment.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserAccountRepo extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);
}
