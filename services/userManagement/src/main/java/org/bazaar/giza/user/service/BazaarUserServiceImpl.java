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
        Optional<BazaarUser> bazaarUserOptional = bazaarUserRepo.findById(userId);
        if (bazaarUserOptional.isEmpty()) {
            throw new BazaarUserException(ErrorMessage.USER_ID_NOT_FOUND);
        }

        return bazaarUserOptional.get();
    }

    @Override
    public String deleteUser(Long userId) {
        bazaarUserRepo.deleteById(userId);

        return "User deleted Successfully.";
    }

}
