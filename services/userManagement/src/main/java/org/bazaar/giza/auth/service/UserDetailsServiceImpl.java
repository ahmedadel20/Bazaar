package org.bazaar.giza.auth.service;

import java.util.Optional;

import org.bazaar.giza.auth.entity.UserInfoDetails;
import org.bazaar.giza.auth.entity.UserRoles;
import org.bazaar.giza.auth.exception.AuthException;
import org.bazaar.giza.auth.repo.UserRolesRepo;
import org.bazaar.giza.constant.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRolesRepo repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        Optional<UserRoles> userDetail = repository.findByUser_Email(email);

        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new AuthException(ErrorMessage.EMAIL_NOT_FOUND));
    }
}
