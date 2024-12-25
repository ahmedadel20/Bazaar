package org.bazaar.giza.user.service;

import org.bazaar.giza.user.entity.BazaarUser;

// TODO: Add update User method
public interface BazaarUserService {
    BazaarUser createUser(BazaarUser bazaarUser);

    BazaarUser getSingleUser(Long userId);

    String deleteUser(Long userId);
}
