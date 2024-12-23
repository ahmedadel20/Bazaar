package org.bazaar.giza.customer.repo;

import org.bazaar.giza.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
