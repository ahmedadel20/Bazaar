package org.bazaar.giza.user.service;

import java.util.Optional;

import org.bazaar.giza.constant.ErrorMessage;
import org.bazaar.giza.user.entity.BazaarUser;
import org.bazaar.giza.user.exception.BazaarUserException;
import org.bazaar.giza.user.repo.BazaarUserRepo;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BazaarUserServiceImpl implements BazaarUserService {
    private final BazaarUserRepo bazaarUserRepo;

    @Override
    public BazaarUser createUser(BazaarUser bazaarUser) {
        boolean emailExists = bazaarUserRepo.findByEmail(bazaarUser.getEmail()).isPresent();
        boolean phoneExists = bazaarUserRepo.findByPhoneNumber(bazaarUser.getPhoneNumber()).isPresent();
        // Duplicate email or phone number are not allowed
        if (emailExists && phoneExists) {
            throw new BazaarUserException(
                    String.format("%s\n%s", ErrorMessage.DUPLICATE_EMAIL, ErrorMessage.DUPLICATE_PHONE_NUMBER));
        } else if (emailExists) {
            throw new BazaarUserException(ErrorMessage.DUPLICATE_EMAIL);
        } else if (phoneExists) {
            throw new BazaarUserException(ErrorMessage.DUPLICATE_PHONE_NUMBER);
        }

        return bazaarUserRepo.save(bazaarUser);
    }

    @Override
    public BazaarUser getSingleUser(Long userId) {
        return searchId(userId);
    }

    @Override
    public String deleteUser(Long userId) {
        if (userId == null) {
            throw new BazaarUserException(ErrorMessage.ID_CANNOT_BE_NULL);
        }

        bazaarUserRepo.delete(searchId(userId));
        return "User deleted Successfully.";
    }

    // Helper Functions
    private BazaarUser searchId(Long id) {
        if (id == null) {
            throw new BazaarUserException(ErrorMessage.ID_CANNOT_BE_NULL);
        }

        Optional<BazaarUser> bazaarUserOptional = bazaarUserRepo.findById(id);
        if (bazaarUserOptional.isEmpty()) {
            throw new BazaarUserException(ErrorMessage.USER_ID_NOT_FOUND);
        }
        return bazaarUserOptional.get();
    }
}
