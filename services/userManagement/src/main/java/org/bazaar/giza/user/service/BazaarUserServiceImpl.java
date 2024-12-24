package org.bazaar.giza.user.service;

import java.util.Optional;

import org.bazaar.giza.user.entity.BazaarUser;
import org.bazaar.giza.user.repo.BazaarUserRepo;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BazaarUserServiceImpl implements BazaarUserService {
    private final BazaarUserRepo bazaarUserRepo;

    @Override
    public BazaarUser createUser(BazaarUser bazaarUser) {
        // Duplicate email or phone number are not allowed
        if (bazaarUserRepo.findByEmail(bazaarUser.getEmail()).isPresent()
                || bazaarUserRepo.findByPhoneNumber(bazaarUser.getPhoneNumber()).isPresent()) {
            // FIXME: Replace with custom exception
            throw new RuntimeException();
        }
        
        return bazaarUserRepo.save(bazaarUser);
    }

    @Override
    public BazaarUser getSingleUser(Long userId) {
        Optional<BazaarUser> bazaarUserOptional = bazaarUserRepo.findById(userId);
        if (bazaarUserOptional.isEmpty()) {
            // FIXME: Replace with custom exception
            throw new RuntimeException();
        }

        return bazaarUserOptional.get();
    }

    @Override
    public String deleteUser(Long userId) {
        bazaarUserRepo.deleteById(userId);

        return "User deleted Successfully.";
    }

}
