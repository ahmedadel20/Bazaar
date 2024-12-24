package org.bazaar.giza.user.service;

import org.bazaar.giza.user.entity.BazaarUser;

public interface BazaarUserService {
    BazaarUser createUser(BazaarUser bazaarUser);

    BazaarUser getSingleUser(Long userId);

    String deleteUser(Long userId);
}
