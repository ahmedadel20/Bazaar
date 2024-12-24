package org.bazaar.giza.auth.repo;

import org.bazaar.giza.auth.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepo extends JpaRepository<UserRoles, Long> {
}
